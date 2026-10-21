package ru.my.projects.booking.arena;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.my.projects.booking.city.City;

import java.time.LocalDateTime;

@Entity
@Table(name = "arenas")
public class Arena {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String address;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Getter
    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @Setter
    @Getter
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    public Arena(String name, String description, String address, City city){
        this.name = name; this.description = description; this.city = city; this.address = address;
        this.addedAt = LocalDateTime.now(); this.changedAt = LocalDateTime.now();
    }

    public Arena(){

    }
}
