package ru.my.projects.booking.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserCreateDTO(
        @NotBlank(message = "У пользователя должно быть полное имя!") String fullName,
        @NotNull(message = "У пользователя обязан быть email!") @Email(message = "Должен быть корректный и существующий email!") String email,
        @NotNull(message = "У пользователя должна быть указана дата рождения!") @Past(message = "День рождения должен быть раньше текущей даты") LocalDate birthDate,
        BigDecimal balance
) {

}
