package ru.my.projects.booking.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.my.projects.booking.hall.Hall;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventDTO(
        Long id,
        @NotBlank(message = "События без названия не может быть!") @Size(max = 50, message = "Слишком длинное имя для события!") String title,
        @NotNull(message = "У события должно быть начало!") LocalDateTime startsAt,
        @NotNull(message = "Событие не может быть вечным!") LocalDateTime endsAt,
        BigDecimal price,
        @NotNull(message = "У события долже быть зал, где все роходит") Hall hall,
        int ageRestrict
) {
    public EventDTO transfer(Event event){
        return new EventDTO(event.getId(), event.getTitle(), event.getStartsAt(), event.getEndsAt(), event.getPrice(), event.getHall(), event.getAgeRestrict());
    }
}
