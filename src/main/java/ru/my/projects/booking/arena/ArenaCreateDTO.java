package ru.my.projects.booking.arena;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArenaCreateDTO(
        @NotBlank(message = "У арены должно быть имя!") @Size(max = 50, message = "Слишком длинное название для арены!") String name,
        String description,
        @NotBlank(message = "Адрес не должен быть пустым!") @Size(max = 50, message = "Слишком длинный адрес!") String address,
        @NotBlank(message = "Арена должна находиться в каком-то городе!") String cityName
){

}
