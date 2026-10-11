package ru.my.projects.booking.hall;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import ru.my.projects.booking.arena.Arena;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "halls")
public class Hall {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String name;

    @Getter
    private String description;

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

    public Hall(String name, String description, Arena arena){
        this.name = name; this.description = description; this.arena = arena;
        this.addedAt = LocalDateTime.now();
    }
}
