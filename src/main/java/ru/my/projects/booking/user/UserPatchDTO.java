package ru.my.projects.booking.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserPatchDTO(
        Long id,
        String fullName,
        @Email(message = "Должно быть корректный и существующий email!") String email,
        @Past(message = "День рождения должен быть раньше текущей даты") LocalDate birthDate,
        BigDecimal balance
) {
}
