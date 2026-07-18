package com.example.employeehierarchy.repository;

import com.example.employeehierarchy.entity.User;
import com.example.employeehierarchy.entity.WorkspaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(Long userId);
    List<User> findByWorkspace(WorkspaceRole workspace);
}

