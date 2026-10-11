package ru.my.projects.booking.user;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String fullName;

    @Getter
    @Column(name = "email", unique = true)
    private String email;

    @Getter
    private Integer age;

    @Getter
    private LocalDateTime createdAt;

    public User(String fullName, String email, Integer age){
        this.fullName = fullName; this.email = email; this.age = age;
        this.createdAt = LocalDateTime.now();
    }
}
