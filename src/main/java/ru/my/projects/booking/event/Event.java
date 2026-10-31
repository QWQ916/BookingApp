package ru.my.projects.booking.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @Setter
    @Getter
    private String title;

    @Setter
    @Getter
    private LocalDateTime startsAt;

    @Setter
    @Getter
    private LocalDateTime endsAt;

    @Setter
    @Getter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Setter
    @Getter
    private int ageRestrict;

    public Event(String title, LocalDateTime startsAt, LocalDateTime endsAt, BigDecimal price, Hall hall, int ageRestrict){
        this.title = title; this.startsAt = startsAt; this.endsAt = endsAt; this.price = price; this.hall = hall; this.ageRestrict = ageRestrict;
    }

    public Event(){

    }
}
