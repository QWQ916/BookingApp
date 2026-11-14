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
// - CRUD tickets | Поиск по имени билета через параметры
// - Сделать свои обработки ошибок и ловлю глобальным обработчиком

// - Сделать таблицу для изменений баланса у юзеров (id, user_id, balance_type (REFUND, DEPOSIT, WITHDRAW, PAID), event_id (if PAID or REFUND), amount, date)