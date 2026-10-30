package ru.my.projects.booking.hall;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record HallPatchDTO(
        @Min(message = "В зале не могут отсутствовать места", value = 1) @Max(value = 100, message = "В зале не может быть больше ста рядов") Integer rowsCount,
        @Min(message = "В зале не могут отсутствовать места", value = 1) @Max(value = 1_000, message = "В зале не может быть больше тысячи мест") Integer seatsInRows
) {
}
