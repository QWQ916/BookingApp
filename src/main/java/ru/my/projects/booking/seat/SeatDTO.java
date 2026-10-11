package ru.my.projects.booking.seat;

import jakarta.validation.constraints.NotNull;
import ru.my.projects.booking.hall.Hall;

public record SeatDTO(
        Long id,
        @NotNull(message = "У места есть ряд!") Integer row,
        @NotNull(message = "У места есть номер!") Integer seat_num,
        @NotNull(message = "Место должно находиться в каком-то зале!") Hall hall
) {
    public SeatDTO transfer(Seat seat){
        return new SeatDTO(seat.getId(), seat.getRow(), seat.getSeatNum(), seat.getHall());
    }
}
