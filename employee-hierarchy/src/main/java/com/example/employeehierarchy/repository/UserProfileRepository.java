package com.example.employeehierarchy.repository;

import com.example.employeehierarchy.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
    Optional<UserProfile> findByLoginId(String loginId);
    List<UserProfile> findByParentId(Long parentId);

    @Query("SELECT up FROM UserProfile up WHERE up.parentId = up.userId")
    List<UserProfile> findAllManagers();

    @Query("SELECT up FROM UserProfile up WHERE up.parentId != up.userId")
    List<UserProfile> findAllEmployees();
}

