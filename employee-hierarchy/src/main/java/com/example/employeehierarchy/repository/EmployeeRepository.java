package com.example.employeehierarchy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
    List<Employee> findByManagerId(Long managerId);

    @Query("SELECT e FROM Employee e WHERE e.managerId IS NULL")
    List<Employee> findTopLevelManagers();
}