package ru.my.projects.booking.hall;

import jakarta.validation.constraints.*;

public record HallCreateDTO(
        @NotBlank(message = "У зала должно быть имя!") @Size(max = 30, message = "Слишком большое название зала") String name,
        String description,
        @NotNull(message = "Залу нужно количество мест в нём!") @Min(message = "В зале не могут отсутствовать места", value = 1) @Max(value = 1_000, message = "В зале не может быть больше тысячи мест") Integer seatsInRows,
        @NotNull(message = "В зале обязано быть конкретное количество мест!") @Min(message = "В зале не могут отсутствовать места", value = 1) @Max(value = 100, message = "В зале не может быть больше ста рядов") Integer rowsCount
) {
}
