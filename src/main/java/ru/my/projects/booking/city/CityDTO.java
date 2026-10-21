package ru.my.projects.booking.city;

import java.time.LocalDateTime;

public record CityDTO(
        Long id,
        String name,
        LocalDateTime addedAt
) {
    public static CityDTO transfer(City city){
        return new CityDTO(city.getId(), city.getName(), city.getAddedAt());
    }
}
