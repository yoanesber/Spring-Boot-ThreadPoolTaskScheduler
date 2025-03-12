package com.yoanesber.spring.task_scheduler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 20, unique = true)
    private String userName;

    @Column(nullable = false, length = 150)
    private String password;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "firstname", nullable = false, length = 20)
    private String firstName;

    @Column(name = "lastname", length = 20)
    private String lastName;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isEnabled = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isAccountNonExpired = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isAccountNonLocked = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isCredentialsNonExpired = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    private Instant accountExpirationDate;
    private Instant credentialsExpirationDate;
    private Instant lastLogin;

    @Column(nullable = false, length = 15)
    private String userType;

    @Column(nullable = false, length = 20)
    private String createdBy;

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant createdDate = Instant.now();

    @Column(nullable = false, length = 20)
    private String updatedBy;

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant updatedDate = Instant.now();
}
