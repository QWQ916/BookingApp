package ru.my.projects.booking.arena;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.my.projects.booking.city.City;

public record ArenaDTO(
        Long id,
        @NotBlank(message = "У арены должно быть имя!") @Size(max = 50, message = "Слишком длинное название для арены!") String name,
        String description,
        @NotBlank(message = "Адрес не должен быть пустым!") @Size(max = 50, message = "Слишком длинный адрес!") String address,
        @NotNull(message = "Арена должна находиться в каком-то городе!") City city
) {
    public ArenaDTO transfer(Arena arena){
        return new ArenaDTO(arena.getId(), arena.getName(), arena.getDescription(), arena.getAddress(), arena.getCity());
    }
}
