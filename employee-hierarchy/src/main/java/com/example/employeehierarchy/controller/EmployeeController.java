package com.example.employeehierarchy.controller;

import com.example.employeehierarchy.dto.EmployeeDetailsResponse;
import com.example.employeehierarchy.dto.ManagerDetailsResponse;
import com.example.employeehierarchy.security.JwtUtil;
import com.example.employeehierarchy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class EmployeeController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public EmployeeController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/getDetails")
    public ResponseEntity<Object> getDetails(HttpServletRequest request) {

        try {

            String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {

                EmployeeDetailsResponse response = new EmployeeDetailsResponse();
                response.setMessage("Missing or invalid Authorization header");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = header.substring(7);

            if (!jwtUtil.validateToken(token)) {

                EmployeeDetailsResponse response = new EmployeeDetailsResponse();
                response.setMessage("Invalid or expired token");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Long userId = jwtUtil.extractUserId(token);
            String workspaceId = jwtUtil.extractWorkspaceId(token);

            Object response = userService.getUserDetails(userId,workspaceId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            EmployeeDetailsResponse response = new EmployeeDetailsResponse();
            response.setMessage("Error retrieving user details : " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<Object> getUser(HttpServletRequest request) {
        return getDetails(request);
    }
}