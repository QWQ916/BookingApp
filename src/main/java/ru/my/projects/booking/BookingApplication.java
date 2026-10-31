package ru.my.projects.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

}

// Исправить валидаци даты при PATCH (выдаёт ошибку) + на GET в списке повесить параметры поиска по имени