package com.example.employeehierarchy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDetailsResponse {

    private UserProfileDTO userProfile;
    private List<UserProfileDTO> employees;
    private String message;
}