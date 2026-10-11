package ru.my.projects.booking.event;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.cglib.core.Local;
import ru.my.projects.booking.hall.Hall;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String title;

    @Getter
    private LocalDateTime startsAt;

    @Getter
    private LocalDateTime endsAt;

    @Getter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Getter
    private int ageRestrict;

    public Event(String title, LocalDateTime startsAt, LocalDateTime endsAt, BigDecimal price, Hall hall, int ageRestrict){
        this.title = title; this.startsAt = startsAt; this.endsAt = endsAt; this.price = price; this.hall = hall; this.ageRestrict = ageRestrict;
    }
}
