package ru.my.projects.booking.hall;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.my.projects.booking.arena.Arena;

public record HallDTO(
        Long id,
        @NotBlank(message = "У зала должно быть имя!") @Size(max = 30, message = "Слишком большое название зала") String name,
        String description,
        @NotNull(message = "У зала должна быть арена!") Arena arena
) {
    public HallDTO transfer(Hall hall){
        return new HallDTO(hall.getId(), hall.getName(), hall.getDescription(), hall.getArena());
    }
}
