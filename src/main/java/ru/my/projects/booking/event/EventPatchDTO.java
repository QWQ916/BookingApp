package ru.my.projects.booking.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventPatchDTO(
        @Size(max = 50, message = "Слишком длинное имя для события!") String title,

        @JsonFormat(pattern = "dd-MM-yyyy | HH:mm:ss")
        @Future(message = "Событие не может начаться раньше сегодня")
        LocalDateTime startsAt,

        @JsonFormat(pattern = "dd-MM-yyyy | HH:mm:ss")
        @Future(message = "Событие не может закончиться раньше сегодня")
        LocalDateTime endsAt,

        Long hallId
) {
}
