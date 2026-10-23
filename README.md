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

Проект развивается поэтапно: от монолита на Spring Boot до микросервисов с кэшем и очередью сообщений. Прогресс по этапам отмечен в разделе [Roadmap](#-roadmap).

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
| Язык и платформа | Java 17, Spring Boot 4, Maven | ✅ |
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

```mermaid
erDiagram
    CITIES  ||--o{ ARENAS  : "в городе"
    ARENAS  ||--o{ HALLS   : "содержит"
    HALLS   ||--o{ SEATS   : "схема мест"
    HALLS   ||--o{ EVENTS  : "проходит в"
    EVENTS  ||--o{ TICKETS : "продаются на"
    SEATS   ||--o{ TICKETS : "занимает"
    USERS   ||--o{ TICKETS : "покупает"

    CITIES {
        bigint id PK
        varchar name
    }
    ARENAS {
        bigint id PK
        varchar name
        varchar address
        bigint city_id FK
    }
    HALLS {
        bigint id PK
        varchar name
        bigint arena_id FK
    }
    SEATS {
        bigint id PK
        int row
        int seat_num
        bigint hall_id FK
    }
    EVENTS {
        bigint id PK
        varchar title
        timestamp starts_at
        timestamp ends_at
        numeric price
        int age_restrict
        bigint hall_id FK
    }
    USERS {
        bigint id PK
        varchar full_name
        int age
    }
    TICKETS {
        bigint id PK
        varchar status
        bigint event_id FK
        bigint seat_id FK
        bigint user_id FK
        timestamp created_at
    }
```

**Логика:** город → арены → залы → места. Мероприятие проходит в конкретном зале, поэтому на него доступны места именно этого зала. Билет связывает мероприятие, место и пользователя.

Схема создаётся только миграциями Flyway (`src/main/resources/db/migration`), Hibernate её лишь проверяет (`ddl-auto: validate`).

---

## Как защищено двойное бронирование *(этапы 2-3)*

```mermaid
sequenceDiagram
    actor A as Пользователь A
    actor B as Пользователь B
    participant S as Booking API
    participant K as KeyDB
    participant DB as PostgreSQL

    A->>S: забронировать место 42
    B->>S: забронировать место 42
    S->>K: SET hold:event:42 A NX EX 600
    K-->>S: OK (место удержано за A)
    S->>K: SET hold:event:42 B NX EX 600
    K-->>S: nil (уже занято)
    S-->>B: 409 Conflict: место занято
    A->>S: подтвердить оплату
    S->>DB: INSERT ticket (уникальный индекс event_id + seat_id)
    S-->>A: 201 Created
```

1. **Уникальный индекс в PostgreSQL** на пару «мероприятие + место» для активных билетов: база физически не даст создать второй билет на то же место.
2. **Удержание места в KeyDB** атомарной командой `SET NX EX`: место «держится» за покупателем 10 минут и освобождается само, если оплату не подтвердили.
3. **Тест конкурентности:** 10 потоков одновременно бронируют одно место, успешной должна быть ровно одна бронь.

---

## Целевая архитектура *(этап 4)*

```mermaid
flowchart LR
    C([Клиент]) --> ES[event-service<br/>арены, залы, места,<br/>мероприятия]
    C --> BS[booking-service<br/>билеты, удержание мест]
    BS -- REST: проверка места --> ES
    BS -- удержание мест, TTL --> K[(KeyDB)]
    BS -- BookingConfirmed --> MQ{{RabbitMQ}}
    MQ --> NS[notification-service<br/>уведомления]
    ES --> DB1[(PostgreSQL<br/>events)]
    BS --> DB2[(PostgreSQL<br/>booking)]
    NS --> DB3[(PostgreSQL<br/>notifications)]
```

- **Синхронно (REST):** booking-service проверяет в event-service, что место существует и относится к залу мероприятия.
- **Асинхронно (RabbitMQ):** после подтверждения брони публикуется событие. Сервис уведомлений обрабатывает его независимо, и если он недоступен, бронирование всё равно работает, а сообщения дождутся его в очереди.
- **Своя база у каждого сервиса:** сервисы не лезут в чужие таблицы напрямую.

---

## Структура проекта

```
BookingApp/
├── docker-compose.yaml              # PostgreSQL для локальной разработки
├── pom.xml
└── src/
    ├── main/
    │   ├── java/ru/my/projects/booking/
    │   │   ├── BookingApplication.java
    │   │   ├── arena/      ⏳ города, арены, залы
    │   │   ├── event/      ⏳ мероприятия, схема мест
    │   │   ├── ticket/     ⏳ бронирование и билеты
    │   │   └── common/     ⏳ обработка ошибок, конфигурация
    │   │       (внутри фич: controller / service / repository / dto / entity)
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
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| PostgreSQL | `localhost:5000`, БД `booking_db` |

---

## API *(планируется)*

| Метод | Эндпоинт | Описание | Статус |
|---|---|---|:---:|
| `GET / POST` | `/api/cities` | Города | ⏳ |
| `GET / POST / PUT / DELETE` | `/api/arenas` | Арены | ⏳ |
| `POST` | `/api/arenas/{id}/halls` | Создать зал сразу со схемой мест (ряды × места) | ⏳ |
| `GET / POST / PUT / DELETE` | `/api/events` | Мероприятия, фильтры по городу, арене и дате, пагинация | ⏳ |
| `GET` | `/api/events/{id}/seats` | Карта мест: свободно / занято | ⏳ |
| `POST` | `/api/tickets` | Забронировать место | ⏳ |
| `POST` | `/api/tickets/{id}/confirm` | Подтвердить бронь | ⏳ |
| `POST` | `/api/tickets/{id}/cancel` | Отменить бронь | ⏳ |
| `GET` | `/api/tickets/my` | Мои билеты | ⏳ |

Ответы об ошибках в едином формате: `400` ошибка валидации, `404` не найдено, `409` место уже занято.

---

## 🗺 Roadmap

### Этап 0. Подготовка
- [x] Проект на Spring Boot (Web MVC, Data JPA, Validation, Flyway, PostgreSQL)
- [x] PostgreSQL в Docker Compose
- [x] Первая миграция Flyway со схемой БД
- [x] `ddl-auto: validate`, `open-in-view: false`
- [x] Подключен Swagger (springdoc-openapi)
- [x] README с описанием проекта и планом

### Этап 1. Каталог
- [ ] Entity для городов, арен, залов, мест и мероприятий, связи `@ManyToOne(fetch = LAZY)`
- [ ] CRUD-эндпоинты, наружу только DTO
- [ ] Создание зала сразу со схемой мест
- [ ] Валидация входных данных (`@Valid`, Bean Validation)
- [ ] Единый формат ошибок через `@RestControllerAdvice`
- [ ] Пагинация и фильтры для мероприятий
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
