<div align="center">

# 🎫 BookingApp

**Backend-сервис для бронирования мест на мероприятия: арены, залы, схема мест и билеты**

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-migrations-CC0200?logo=flyway&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?logo=swagger&logoColor=black)
![Status](https://img.shields.io/badge/status-in_development-orange)

</div>

---

## О проекте

Пользователь выбирает мероприятие, видит схему зала со свободными и занятыми местами и бронирует конкретное место. Главная инженерная задача проекта: **не допустить, чтобы одно место на одно мероприятие продали дважды**, даже если два человека нажали «Купить» в одну и ту же секунду.

Проект развивается поэтапно: от монолита на Spring Boot до микросервисов с кэшем и очередью сообщений. Прогресс по этапам отмечен в разделе [Roadmap](#-roadmap). Актуальная разработка ведётся в ветке `dev`.

**Что здесь можно посмотреть:**

- проектирование реляционной схемы и версионирование БД через миграции;
- слоистую архитектуру Spring Boot: controller → service → repository, DTO, валидация;
- работу с транзакциями и конкурентным доступом к данным *(этап 2)*;
- кэширование и временное удержание мест в KeyDB *(этап 3)*;
- микросервисы и асинхронное взаимодействие через RabbitMQ *(этап 4)*.

---

## Стек

| Область | Технологии | Статус |
|---|---|:---:|
| Язык и платформа | Java 17, Spring Boot 4, Maven, Lombok | ✅ |
| Web и API | Spring Web MVC, Bean Validation, springdoc-openapi (Swagger UI) | ✅ |
| Данные | PostgreSQL 17, Spring Data JPA (Hibernate), Flyway | ✅ |
| Инфраструктура | Docker, Docker Compose | ✅ |
| Тесты | JUnit 5, Mockito, Testcontainers | ⏳ |
| Кэш и удержание мест | KeyDB (Redis-совместимый), Spring Cache | ⏳ |
| Очереди сообщений | RabbitMQ, Spring AMQP | ⏳ |
| Микросервисы | REST между сервисами, отдельная БД на сервис, Spring Boot Actuator | ⏳ |

✅ уже в проекте · ⏳ запланировано

---

## Модель данных

| Таблица | Что хранит |
|---|---|
| `cities` | Города (название уникально) |
| `arenas` | Арены: название, описание, адрес, город |
| `halls` | Залы внутри арены |
| `seats` | Места в зале: ряд и номер. Комбинация «зал + ряд + место» уникальна |
| `events` | Мероприятия: название, время начала и конца, цена, возрастное ограничение, зал |
| `users` | Пользователи: ФИО, email (уникален), возраст |
| `tickets` | Билеты: мероприятие, место, пользователь и статус (`HELD`, `CONFIRMED`, `CANCELLED`, `CHECKED`) |

**Логика:** город → арены → залы → места. Мероприятие проходит в конкретном зале, поэтому на него доступны места именно этого зала. Билет связывает мероприятие, место и пользователя.

Схема создаётся только миграциями Flyway (`src/main/resources/db/migration`), Hibernate её лишь проверяет (`ddl-auto: validate`).

---

## Как защищено двойное бронирование *(этапы 2-3)*

1. **Уникальный индекс в PostgreSQL** на пару «мероприятие + место» для активных билетов: база физически не даст создать второй билет на то же место.
2. **Удержание места в KeyDB** атомарной командой `SET NX EX`: место «держится» за покупателем 10 минут и освобождается само, если оплату не подтвердили.
3. **Тест конкурентности:** 10 потоков одновременно бронируют одно место, успешной должна быть ровно одна бронь.

---

## Целевая архитектура *(этап 4)*

- **Синхронно (REST):** booking-service проверяет в event-service, что место существует и относится к залу мероприятия.
- **Асинхронно (RabbitMQ):** после подтверждения брони публикуется событие. Сервис уведомлений обрабатывает его независимо, и если он недоступен, бронирование всё равно работает, а сообщения дождутся его в очереди.
- **Своя база у каждого сервиса:** сервисы не лезут в чужие таблицы напрямую.

---

## Структура проекта

Код разложен по фичам: в каждом пакете лежат сущность, DTO, репозиторий, а по мере готовности сервис и контроллер.

```
BookingApp/
├── docker-compose.yaml                  # PostgreSQL для локальной разработки
├── pom.xml
└── src/
    ├── main/
    │   ├── java/ru/my/projects/booking/
    │   │   ├── BookingApplication.java
    │   │   ├── city/       ✅ entity, DTO, repository, service, controller
    │   │   ├── arena/      ✅ entity, DTO, repository, service, controller
    │   │   ├── hall/       ✅ entity, DTO, repository, service, controller
    │   │   ├── seat/       ⏳ entity, DTO, repository
    │   │   ├── event/      ⏳ entity, DTO, repository
    │   │   ├── user/       ⏳ entity, DTO, repository
    │   │   ├── ticket/     ⏳ entity, DTO, repository
    │   │   ├── enums/      TicketStatus
    │   │   └── common/
    │   │       └── exception/   GlobalExceptionHandler, ResourceNotFoundException
    │   └── resources/
    │       ├── application.yml
    │       └── db/migration/
    │           └── V1__create_tables.sql
    └── test/java/ru/my/projects/booking/
```

---

## Запуск

**Нужно:** JDK 17+, Docker.

```bash
# 1. Поднять PostgreSQL (порт 5000 на хосте)
docker compose up -d

# 2. Запустить приложение (Flyway сам применит миграции)
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

| Что | Адрес |
|---|---|
| API | http://localhost:8080/api/0 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| PostgreSQL | `localhost:5000`, БД `booking_db` |

---

## API

Базовый путь: `/api/0`

| Метод | Эндпоинт | Описание | Статус |
|---|---|---|:---:|
| `GET / POST` | `/cities` | Список городов, добавить город | ✅ |
| `GET / PUT / DELETE` | `/cities/{id}` | Получить, изменить, удалить город | ✅ |
| `GET / POST` | `/arenas` | Список арен, добавить арену (город указывается по названию) | ✅ |
| `GET / PUT / DELETE` | `/arenas/{id}` | Получить, изменить, удалить арену | ✅ |
| `POST` | `/arenas/{id}/halls` | Создать зал сразу со схемой мест (ряды × места) | ✅⏳ |
| `GET / POST / PUT / DELETE` | `/events` | Мероприятия, фильтры по городу, арене и дате, пагинация | ⏳ |
| `GET` | `/events/{id}/seats` | Карта мест: свободно / занято | ⏳ |
| `POST` | `/tickets` | Забронировать место | ⏳ |
| `POST` | `/tickets/{id}/confirm` | Подтвердить бронь | ⏳ |
| `POST` | `/tickets/{id}/cancel` | Отменить бронь | ⏳ |
| `GET` | `/tickets/my` | Мои билеты | ⏳ |

Ответы об ошибках в едином формате (`status`, `error`, `message`). Сейчас обрабатывается `404` не найдено, в планах `400` ошибка валидации и `409` место уже занято.

---

## 🗺 Roadmap

### Этап 0. Подготовка
- [x] Проект на Spring Boot (Web MVC, Data JPA, Validation, Flyway, PostgreSQL, Lombok)
- [x] PostgreSQL в Docker Compose
- [x] Первая миграция Flyway со схемой БД
- [x] `ddl-auto: validate`, `open-in-view: false`
- [x] Подключен Swagger (springdoc-openapi)
- [x] README с описанием проекта и планом

### Этап 1. Каталог
- [x] Entity для всех таблиц, связи `@ManyToOne(fetch = LAZY)`
- [x] DTO на Java records, валидация входных данных в DTO
- [x] Репозитории Spring Data JPA для всех сущностей
- [x] Глобальный обработчик ошибок `@RestControllerAdvice` (404)
- [x] CRUD для городов
- [x] CRUD для арен
- [x] Убрать `@Transactional` из контроллеров: маппинг в DTO внутри сервиса или `JOIN FETCH` в репозитории
- [ ] Обработка ошибок валидации (400)
- [x] CRUD для залов и создание зала сразу со схемой мест
- [ ] CRUD для мероприятий, пагинация и фильтры
- [ ] Карта мест мероприятия (JOIN мест и билетов)
- [ ] Unit-тесты сервисов (JUnit 5, Mockito)

### Этап 2. Бронирование
- [ ] Жизненный цикл билета: бронь → подтверждение → отмена
- [ ] Уникальный индекс против двойной брони, ошибка `409`
- [ ] Транзакции (`@Transactional`) и автоматическое снятие неоплаченных броней
- [ ] «Мои билеты» одним запросом без N+1 (JOIN FETCH / DTO-проекция)
- [ ] Тест конкурентности: 10 потоков на одно место, успешна одна бронь
- [ ] Интеграционные тесты на Testcontainers

### Этап 3. KeyDB
- [ ] Удержание места на 10 минут (`SET NX EX`)
- [ ] Кэш списка мероприятий (`@Cacheable` / `@CacheEvict`)

### Этап 4. Микросервисы
- [ ] Разделение на event-service, booking-service, notification-service
- [ ] Отдельная БД у каждого сервиса
- [ ] Событие `BookingConfirmed` через RabbitMQ
- [ ] Идемпотентная обработка сообщений
- [ ] Вся система запускается одной командой `docker compose up`
- [ ] Health-check через Spring Boot Actuator

### Этап 5. Дополнительно
- [ ] Spring Security + JWT, роли USER / ADMIN
- [ ] API Gateway
- [ ] CI на GitHub Actions: сборка и тесты на каждый push

---

<div align="center">

**Автор:** Иван Широковских · [GitHub](https://github.com/QWQ916) · shirok2005@icloud.com

</div>
