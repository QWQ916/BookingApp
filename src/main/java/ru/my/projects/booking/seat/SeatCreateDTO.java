package ru.my.projects.booking.seat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SeatCreateDTO(
        @NotNull(message = "У места есть ряд!") @Min(value = 1, message = "Ряд не может быть неположительным!") Integer row,
        @NotNull(message = "У места есть номер!") @Min(value = 1, message = "Место не может быть неположительным!") Integer seat_num
) {
}
