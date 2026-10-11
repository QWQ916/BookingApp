package ru.my.projects.booking.city;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "cities")
public class City {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(unique = true)
    private String name;

    @Getter
    @Column(name = "added_at")
    private LocalDateTime addedAt;

    public City(String name){
        this.name = name;
        this.addedAt = LocalDateTime.now();
    }
}
