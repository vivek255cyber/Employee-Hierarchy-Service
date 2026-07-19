package com.example.employeehierarchy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String loginId;

    private String country;

    @Column(nullable = true)
    private Long parentId; // For MANAGER: parentId = userId, For EMPLOYEE: parentId = manager's userId

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Helper method to check if this profile is a manager
     * A manager has parentId equal to userId
     */
    /*public boolean isManager() {
        return this.userId.equals(this.parentId);
    }*/
}