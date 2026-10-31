package ru.my.projects.booking.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventDTO(
        Long id,
        String title,

        @JsonFormat(pattern = "dd.MM.yyyy | HH:mm:ss")
        LocalDateTime startsAt,

        @JsonFormat(pattern = "dd.MM.yyyy | HH:mm:ss")
        LocalDateTime endsAt,

        BigDecimal price,
        String hallName,
        String arenaName,
        int ageRestrict
) {
    public static EventDTO transfer(Event event){
        return new EventDTO(event.getId(), event.getTitle(), event.getStartsAt(), event.getEndsAt(), event.getPrice(), event.getHall().getArena().getName(), event.getHall().getName(), event.getAgeRestrict());
    }
}
