package ru.my.projects.booking.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CityDTO(
        Long id,
        @NotBlank(message = "У города есть название!") @Size(max = 20, message = "Слишком длинное имя для города!") String name,
        LocalDateTime addedAt
) {
    public static CityDTO transfer(City city){
        return new CityDTO(city.getId(), city.getName(), city.getAddedAt());
    }
}
