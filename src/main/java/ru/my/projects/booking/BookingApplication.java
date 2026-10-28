package ru.my.projects.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

}

// ДОДЕЛАТЬ SEATS, чтобы при создании сразу было добавление рядов и мест (указать кол-во рядов и кол-во мест вместо максимального кол-во мест)