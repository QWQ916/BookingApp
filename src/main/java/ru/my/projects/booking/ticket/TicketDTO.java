package ru.my.projects.booking.ticket;

import jakarta.validation.constraints.NotNull;
import ru.my.projects.booking.enums.TicketStatus;
import ru.my.projects.booking.event.Event;
import ru.my.projects.booking.seat.Seat;
import ru.my.projects.booking.user.User;

public record TicketDTO(
        Long id,
        @NotNull(message = "У каждого билета должен быть конкретный статус!") TicketStatus status,
        @NotNull(message = "Каждый билет относится к конкретному событию!") Event event,
        @NotNull(message = "Каждый билет содержит пользователя, купившего его!") User user,
        @NotNull(message = "Каждый билет покупается на конкретное место!") Seat seat
) {
    public TicketDTO transfer(Ticket ticket){
        return new TicketDTO(ticket.getId(), ticket.getStatus(), ticket.getEvent(), ticket.getUser(), ticket.getSeat());
    }
}
