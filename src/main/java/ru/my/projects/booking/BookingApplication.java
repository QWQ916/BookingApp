package ru.my.projects.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

}

// stage-1 (доделать):
// - CRUD users (+balance), tickets | Поиск по имени юзера и билета через параметры
// - Сделать свои обработки ошибок и ловлю глобальным обработчиком