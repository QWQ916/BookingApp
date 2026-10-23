package ru.my.projects.booking.ticket;

import jakarta.persistence.*;
import lombok.Getter;
import ru.my.projects.booking.enums.TicketStatus;
import ru.my.projects.booking.event.Event;
import ru.my.projects.booking.seat.Seat;
import ru.my.projects.booking.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private LocalDateTime createdAt;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Getter
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    public Ticket(TicketStatus status, Event event, User user, Seat seat){
        this.status = status; this.event = event; this.user = user; this.seat = seat;
        this.createdAt = LocalDateTime.now();
    }

    public Ticket(){

    }
}
