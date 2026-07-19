package com.example.employeehierarchy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailsResponse {

    private UserProfileDTO userProfile;
    private String message;
}