package ru.my.projects.booking.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDTO(
        Long id,
        String fullName,
        String email,
        LocalDate age,
        BigDecimal balance
) {
    public static UserDTO transfer(User user){
        return new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getBirthDate(), user.getBalance());
    }
}
