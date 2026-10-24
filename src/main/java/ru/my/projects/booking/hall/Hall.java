package ru.my.projects.booking.hall;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.my.projects.booking.arena.Arena;

import java.time.LocalDateTime;

@Entity
@Table(name = "halls")
public class Hall {
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arena_id")
    private Arena arena;

    @Getter
    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @Setter
    @Getter
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @Setter
    @Getter
    @Column(name = "max_count_seats")
    private Integer maxCountSeats;


    public Hall(String name, String description, Arena arena, Integer maxCountSeats){
        this.name = name; this.description = description; this.arena = arena;
        this.addedAt = LocalDateTime.now(); this.changedAt = LocalDateTime.now();
        this.maxCountSeats = maxCountSeats;
    }

    public Hall(){

    }
}
