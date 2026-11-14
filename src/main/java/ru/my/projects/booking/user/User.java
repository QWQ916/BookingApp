package ru.my.projects.booking.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String fullName;

    @Setter
    @Getter
    @Column(name = "email", unique = true)
    private String email;

    @Setter
    @Getter
    private LocalDate birthDate;

    @Getter
    private LocalDateTime createdAt;

    @Getter
    @Setter
    private BigDecimal balance;

    public User(String fullName, String email, LocalDate birthDate){
        this.fullName = fullName; this.email = email; this.birthDate = birthDate;
        this.createdAt = LocalDateTime.now(); balance = new BigDecimal(0);
    }

    public User(){

    }
}
