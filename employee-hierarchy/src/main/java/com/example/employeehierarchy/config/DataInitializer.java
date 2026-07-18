package com.example.employeehierarchy.config;

import com.example.employeehierarchy.entity.User;
import com.example.employeehierarchy.entity.UserProfile;
import com.example.employeehierarchy.entity.WorkspaceRole;
import com.example.employeehierarchy.repository.UserRepository;
import com.example.employeehierarchy.repository.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           UserProfileRepository userProfileRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        // Create Manager 1
        User manager1 = new User();
        manager1.setUsername("manager1");
        manager1.setPassword(passwordEncoder.encode("password123"));
        manager1.setWorkspace(WorkspaceRole.MANAGER);
        manager1.setActive(true);
        manager1 = userRepository.save(manager1);

        UserProfile managerProfile1 = new UserProfile();
        managerProfile1.setFirstName("Alice");
        managerProfile1.setLastName("Manager");
        managerProfile1.setLoginId("alice.manager");
        managerProfile1.setCountry("USA");
        managerProfile1.setParentId(manager1.getUserId()); // For manager: parentId = userId
        managerProfile1.setUser(manager1);
        userProfileRepository.save(managerProfile1);

        // Create Manager 2
        User manager2 = new User();
        manager2.setUsername("manager2");
        manager2.setPassword(passwordEncoder.encode("password123"));
        manager2.setWorkspace(WorkspaceRole.MANAGER);
        manager2.setActive(true);
        manager2 = userRepository.save(manager2);

        UserProfile managerProfile2 = new UserProfile();
        managerProfile2.setFirstName("Bob");
        managerProfile2.setLastName("Manager");
        managerProfile2.setLoginId("bob.manager");
        managerProfile2.setCountry("UK");
        managerProfile2.setParentId(manager2.getUserId());
        managerProfile2.setUser(manager2);
        userProfileRepository.save(managerProfile2);

        // Create Employee 1 (under Manager 1)
        User employee1 = new User();
        employee1.setUsername("employee1");
        employee1.setPassword(passwordEncoder.encode("password123"));
        employee1.setWorkspace(WorkspaceRole.EMPLOYEE);
        employee1.setActive(true);
        employee1 = userRepository.save(employee1);

        UserProfile employeeProfile1 = new UserProfile();
        employeeProfile1.setFirstName("Charlie");
        employeeProfile1.setLastName("Employee");
        employeeProfile1.setLoginId("charlie.employee");
        employeeProfile1.setCountry("USA");
        employeeProfile1.setParentId(manager1.getUserId()); // Parent is Manager 1
        employeeProfile1.setUser(employee1);
        userProfileRepository.save(employeeProfile1);

        // Create Employee 2 (under Manager 1)
        User employee2 = new User();
        employee2.setUsername("employee2");
        employee2.setPassword(passwordEncoder.encode("password123"));
        employee2.setWorkspace(WorkspaceRole.EMPLOYEE);
        employee2.setActive(true);
        employee2 = userRepository.save(employee2);

        UserProfile employeeProfile2 = new UserProfile();
        employeeProfile2.setFirstName("Diana");
        employeeProfile2.setLastName("Employee");
        employeeProfile2.setLoginId("diana.employee");
        employeeProfile2.setCountry("Canada");
        employeeProfile2.setParentId(manager1.getUserId()); // Parent is Manager 1
        employeeProfile2.setUser(employee2);
        userProfileRepository.save(employeeProfile2);

        // Create Employee 3 (under Manager 2)
        User employee3 = new User();
        employee3.setUsername("employee3");
        employee3.setPassword(passwordEncoder.encode("password123"));
        employee3.setWorkspace(WorkspaceRole.EMPLOYEE);
        employee3.setActive(true);
        employee3 = userRepository.save(employee3);

        UserProfile employeeProfile3 = new UserProfile();
        employeeProfile3.setFirstName("Eve");
        employeeProfile3.setLastName("Employee");
        employeeProfile3.setLoginId("eve.employee");
        employeeProfile3.setCountry("UK");
        employeeProfile3.setParentId(manager2.getUserId()); // Parent is Manager 2
        employeeProfile3.setUser(employee3);
        userProfileRepository.save(employeeProfile3);

        System.out.println("Sample data initialized successfully!");
    }
}