package com.example.employeehierarchy.service;

import com.example.employeehierarchy.entity.Employee;
import com.example.employeehierarchy.entity.Role;
import com.example.employeehierarchy.repository.EmployeeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final PermissionService permissionService;

    public EmployeeService(EmployeeRepository repository, PermissionService permissionService) {
        this.repository = repository;
        this.permissionService = permissionService;
    }

    public Employee getEmployeeById(Long id) {
        Employee target = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee requester = repository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        if (!permissionService.canAccessEmployee(requester, target)) {
            throw new RuntimeException("Access Denied: You can only view your own details or your team's details");
        }
        return target;
    }

    public List<Employee> getTeamHierarchy() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee manager = repository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (manager.getRole() != Role.MANAGER) {
            throw new RuntimeException("Only managers can view team hierarchy");
        }

        return getAllSubordinates(manager.getId());
    }

    private List<Employee> getAllSubordinates(Long managerId) {
        List<Employee> directReports = repository.findByManagerId(managerId);
        List<Employee> all = new ArrayList<>(directReports);

        for (Employee emp : directReports) {
            all.addAll(getAllSubordinates(emp.getId()));
        }
        return all;
    }
}