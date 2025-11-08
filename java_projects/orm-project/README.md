# Learning Platform API

Полноценная учебная платформа на Spring Boot / PostgreSQL. Проект демонстрирует, как построить гибкую ORM-модель с богатой бизнес-логикой, REST API, авторизацией и интеграционными тестами. Репозиторий готов к запуску «из коробки»: есть docker-compose для базы, dev-seed данных, профили конфигураций, docker-образ приложения.

## Основные возможности

- **Пользователи:** общий класс `User` с наследниками `Student` и `Teacher`, профили, контакты, статусы, роли (JOINED inheritance).
- **Курсы и контент:** курсы ↔ категории, теги, модули, уроки, задания, учебные ресурсы. Удаление/создание каскадное, коллекции ленивые.
- **Учебный процесс:** зачисления с проверкой уникальности, статусы, итоговые оценки, submissions с версионированием, оценки, отзыв по курсу.
- **Тестирование знаний:** Quiz → Question → AnswerOption (SINGLE_TABLE + зависимые сущности), сохранение результатов QuizSubmission.
- **REST API:** контроллеры `/api/**` покрывают все сущности, есть аналитика `/api/analytics/summary`, централизованный handler ошибок, Swagger UI.
- **Безопасность:** Spring Security + HTTP Basic, методовый доступ через `@PreAuthorize`, встроенные тестовые пользователи.
- **Инфраструктура:** dev/test профили, env-переменные для подключения к БД, DevDataSeeder, Dockerfile, Testcontainers.
- **Тесты:** 15+ интеграционных сценариев (context load, полный e2e, сервисные тесты курсов/зачислений/квизов) поверх Postgres Testcontainers.

## Быстрый старт

```bash
# 1. Поднимаем PostgreSQL (порт 5432)
docker compose up -d

# 2. Запускаем приложение (dev-профиль активируется по умолчанию)
./gradlew bootRun

# 3. Открываем Swagger UI
open http://localhost:8080/swagger-ui.html
```

### Конфигурация

| Профиль | Файл                | Переменные                           | DDL режим |
|---------|--------------------|--------------------------------------|-----------|
| `dev`   | `application.yaml` | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | `update`  |
| `test`  | `application.yaml` | `TEST_DB_URL`, `TEST_DB_USERNAME`, `TEST_DB_PASSWORD` (не нужны при Testcontainers) | `create-drop` |

Активный профиль задаётся переменной `SPRING_PROFILES_ACTIVE` (по умолчанию `dev`).

### Docker-образ

```bash
# сборка
DOCKER_BUILDKIT=1 docker build -t learning-platform:latest .

# запуск (нужно прокинуть настройки БД)
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/learning_db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  learning-platform:latest
```

## Авторизация

| Роль     | Логин / пароль     | Возможности                                              |
|----------|--------------------|-----------------------------------------------------------|
| `ADMIN`  | `admin / admin`    | CRUD всех сущностей, теги, категории, аналитика          |
| `TEACHER`| `teacher / teacher`| Управление курсами, уроками, заданиями, квизами, enrollment|
| `REVIEWER`| `reviewer / reviewer`| Доступ к би аналитике                                    |
| `STUDENT`| создаются через API | Зачисление, сдача заданий, прохождение квизов            |

Swagger UI и `/v3/api-docs` доступны без авторизации.

## REST API (выдержка)

| Метод | Путь                                | Описание                                | Роли |
|-------|-------------------------------------|------------------------------------------|------|
| POST  | `/api/users/students`               | Создать студента                         | ADMIN |
| GET   | `/api/courses/{id}`                 | Получить курс                           | ADMIN, TEACHER, STUDENT |
| POST  | `/api/courses/{id}/lessons`         | Добавить урок в курс                    | ADMIN, TEACHER |
| POST  | `/api/modules`                      | Создать модуль                          | ADMIN, TEACHER |
| POST  | `/api/assignments`                  | Создать задание                         | ADMIN, TEACHER |
| POST  | `/api/enrollments/courses/{cid}/students/{sid}` | Зачислить студента     | ADMIN, TEACHER |
| POST  | `/api/submissions`                  | Отправить решение задания               | STUDENT |
| POST  | `/api/quizzes/{quizId}/submit`      | Сдать квиз                              | STUDENT |
| GET   | `/api/analytics/summary`            | Общая статистика по платформе           | ADMIN, TEACHER, REVIEWER |

Полный контракт см. в Swagger UI или JSON OpenAPI.

### Примеры запросов

```bash
curl -u admin:admin -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Programming","slug":"programming"}'

curl -u teacher:teacher -X POST http://localhost:8080/api/users/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Lee","email":"alice@example.com","studentNo":"STU100","status":"ACTIVE"}'
```

## Данные по умолчанию

В dev-профиле автоматически создаётся набор демо-объектов (преподаватель, студент, категория, курс, модуль, урок, задание, тег). Это делает класс `DevDataSeeder`. Можно быстро авторизоваться и увидеть сущности в Swagger без ручного ввода данных.

## Тестирование

- **Интеграционные тесты:** `LearningPlatformIntegrationTest`, `CourseServiceIntegrationTest`, `EnrollmentServiceIntegrationTest`, `QuizServiceIntegrationTest`, `HibernateApplicationTests`. Все используют Testcontainers+PostgreSQL.
- **Запуск:** `GRADLE_USER_HOME=$PWD/.gradle ./gradlew test`
- Для ускорения повторных запусков Testcontainers кэширует образы (`postgres:16`). Убедитесь, что Docker Desktop запущен.

## Архитектура и структура каталогов

```
src/main/java
├── config/              # security + dev seeding
├── dto/                 # request/response/nested DTOs
├── entity/              # 20+ сущностей БД (люди, курсы, обучение, квизы)
├── enums/               # перечисления статусов
├── mapper/              # MapStruct мапперы
├── repository/          # Spring Data репозитории
├── service/             # бизнес-логика
└── web/                 # REST контроллеры + error handler
```

## Интеграция и DevOps

- **Dockerfile** — multi-stage сборка jar + runtime на Temurin JRE 17.
- **docker-compose.yaml** — поднимает PostgreSQL 16 с healthcheck.
- Готово к включению CI (например, GitHub Actions: `./gradlew test` + docker build).

## Дополнительные заметки

- Все чувствительные настройки читаются из env-переменных, можно безопасно подключать внешние БД.
- Hibernate настроен на LAZY для коллекций; сервисы работают в транзакциях, чтобы избежать `LazyInitializationException`.
- Централизованный `GlobalExceptionHandler` возвращает JSON формата:

```json
{
  "timestamp": "2024-05-01T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": ["title: must not be blank"],
  "path": "/api/courses"
}
```

