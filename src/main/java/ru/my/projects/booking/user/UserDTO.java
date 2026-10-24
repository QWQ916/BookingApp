package ru.my.projects.booking.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
        Long id,
        @NotBlank(message = "У пользователя должно быть полное имя!") String fullName,
        @NotNull(message = "У пользователя обязан быть email!") @Email(message = "Должно быть корректный и существующий email!") String email,
        @NotNull(message = "У пользователя должен быть указан возраст!") Integer age
) {
    public static UserDTO transfer(User user){
        return new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getAge());
    }
}
