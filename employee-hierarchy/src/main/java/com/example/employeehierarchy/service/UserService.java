package com.example.employeehierarchy.service;

import com.example.employeehierarchy.dto.EmployeeDetailsResponse;
import com.example.employeehierarchy.dto.ManagerDetailsResponse;
import com.example.employeehierarchy.dto.UserProfileDTO;
import com.example.employeehierarchy.entity.UserProfile;
import com.example.employeehierarchy.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserProfileRepository userProfileRepository;

    public UserService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public Object getUserDetails(Long userId,String workspaceId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        boolean manager = isManager(userId);

        if ("MANAGER".equalsIgnoreCase(workspaceId)) {

            ManagerDetailsResponse response = new ManagerDetailsResponse();

            response.setUserProfile(convertToDTO(userProfile));

            response.setEmployees(
                    getAllEmployees(userId)
                            .stream()
                            .map(this::convertToDTO)
                            .toList()
            );

            response.setMessage("Manager details retrieved successfully");

            return response;

        } else {

            EmployeeDetailsResponse response = new EmployeeDetailsResponse();

            response.setUserProfile(convertToDTO(userProfile));
            response.setMessage("Employee details retrieved successfully");

            return response;
        }
    }

    private boolean isManager(Long userId) {
        return !userProfileRepository.findByParentId(userId).isEmpty();
    }

    private List<UserProfile> getAllEmployees(Long managerId) {

        List<UserProfile> employees = new ArrayList<>();

        collectEmployees(managerId, employees, new HashSet<>());

        return employees;
    }

    private void collectEmployees(Long managerId,
                                  List<UserProfile> employees,
                                  Set<Long> visited) {

        if (!visited.add(managerId)) {
            return;
        }

        List<UserProfile> directEmployees =
                userProfileRepository.findByParentId(managerId);

        for (UserProfile employee : directEmployees) {

            if (employee.getUserId().equals(managerId)) {
                continue;
            }

            employees.add(employee);

            collectEmployees(employee.getUserId(), employees, visited);
        }
    }

    private UserProfileDTO convertToDTO(UserProfile userProfile) {

        UserProfileDTO dto = new UserProfileDTO();

        dto.setUserId(userProfile.getUserId());
        dto.setFirstName(userProfile.getFirstName());
        dto.setLastName(userProfile.getLastName());
        dto.setLoginId(userProfile.getLoginId());
        dto.setCountry(userProfile.getCountry());
        dto.setParentId(userProfile.getParentId());
        //dto.setManager(isManager(userProfile.getUserId()));

        return dto;
    }
}