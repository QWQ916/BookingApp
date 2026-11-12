package ru.my.projects.booking.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventCreateDTO(
        @NotBlank(message = "События без названия не может быть!") @Size(max = 50, message = "Слишком длинное имя для события!") String title,
        @NotNull(message = "У события должно быть начало!") @Future(message = "Событие не может начаться раньше сегодня") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startsAt,
        @NotNull(message = "Событие не может быть вечным!") @Future(message = "Событие не может закончиться раньше сегодня") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endsAt,
        BigDecimal price,
        int ageRestrict
) {
}
