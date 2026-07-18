package com.example.employeehierarchy.service;

import com.example.employeehierarchy.dto.LoginRequest;
import com.example.employeehierarchy.dto.LoginResponse;
import com.example.employeehierarchy.entity.User;
import com.example.employeehierarchy.entity.UserProfile;
import com.example.employeehierarchy.repository.UserProfileRepository;
import com.example.employeehierarchy.repository.UserRepository;
import com.example.employeehierarchy.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository,
                                UserProfileRepository userProfileRepository,
                                AuthenticationManager authenticationManager,
                                JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) throws AuthenticationException {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Fetch user
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch user profile for loginId
        UserProfile userProfile = userProfileRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        // Generate JWT token with userId, username, loginId, and workspaceId
        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getUsername(),
                userProfile.getLoginId(),
                user.getWorkspace().name()
        );

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setLoginId(userProfile.getLoginId());
        response.setWorkspaceId(user.getWorkspace().name());
        response.setMessage("Authentication successful");

        return response;
    }
}

