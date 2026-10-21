package ru.my.projects.booking.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CityCreateDTO(@NotBlank(message = "У города есть название!") @Size(max = 20, message = "Слишком длинное имя для города!") String name) {
}
