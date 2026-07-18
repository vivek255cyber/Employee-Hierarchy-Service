package com.example.employeehierarchy.service;

import com.example.employeehierarchy.entity.Employee;
import com.example.employeehierarchy.entity.Role;
import com.example.employeehierarchy.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private final EmployeeRepository repository;

    public PermissionService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public boolean canAccessEmployee(Employee requester, Employee target) {
        if (requester.getId().equals(target.getId())) {
            return true;
        }
        if (requester.getRole() != Role.MANAGER) {
            return false;
        }
        return isUnderManager(requester.getId(), target.getId());
    }

    private boolean isUnderManager(Long managerId, Long targetId) {
        if (managerId == null) return false;
        Employee target = repository.findById(targetId).orElse(null);
        if (target == null || target.getManagerId() == null) return false;
        if (target.getManagerId().equals(managerId)) return true;
        return isUnderManager(managerId, target.getManagerId());
    }
}