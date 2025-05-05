BankAPI

BankAPI — это RESTful API-сервис для работы с банковскими операциями, написанный на Go. Проект ориентирован на безопасность, гибкость и поддержку реальных финансовых сценариев.

Основные функции
1) Регистрация и авторизация пользователей с использованием JWT
2) Управление банковскими счетами и балансами
3) Операции пополнения, снятия и перевода средств
4) Поддержка виртуальных карт с генерацией номеров по алгоритму Луна
5) Шифрование карточных данных с использованием PGP и HMAC
6) Система кредитования с расчётом аннуитетных платежей
7) Фоновый шедулер для списаний и штрафов
8) Интеграция с внешними системами:
9) Email через SMTP (Mailtrap)
10) SOAP-запрос к Центральному банку
11) Финансовая аналитика и прогнозирование остатка
12) Встроенные HTML-формы для ручного тестирования API

Старт
1) Установите Go версии 1.22 или выше
2) Создайте файл .env на основе следующего шаблона:

PORT=8080
JWT_SECRET=supersecretkey
DB_DSN=postgres://postgres:postgres@localhost:5432/bank?sslmode=disable
SMTP_HOST=smtp.mailtrap.io
SMTP_PORT=2525
SMTP_USER=your@mail.com
SMTP_LOGIN=your_login
SMTP_PASS=your_pass
RUN_SCHEDULER=true


Создайте базу данных bank в PostgreSQL
Если вы не используете миграции, создайте таблицы вручную:

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
  id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email           TEXT UNIQUE NOT NULL,
  username        TEXT UNIQUE NOT NULL,
  password_hash   TEXT NOT NULL,
  created_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE accounts (
  id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id    INT REFERENCES users(id),
  number     TEXT NOT NULL,
  balance    NUMERIC(15,2) DEFAULT 0,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE transactions (
  id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  from_account_id INT,
  to_account_id   INT,
  amount          NUMERIC(15,2),
  type            TEXT,
  created_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE cards (
  id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id         INT REFERENCES users(id),
  account_id      INT REFERENCES accounts(id),
  number_hmac     TEXT NOT NULL,
  encrypted_data  BYTEA NOT NULL,
  cvv_hash        TEXT NOT NULL,
  created_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE credits (
  id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id        INT REFERENCES users(id),
  account_id     INT REFERENCES accounts(id),
  amount         NUMERIC(15,2) NOT NULL,
  interest_rate  NUMERIC(5,2) NOT NULL,
  months         INT NOT NULL,
  created_at     TIMESTAMP DEFAULT NOW()
);

CREATE TABLE payment_schedules (
  id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  credit_id   INT REFERENCES credits(id),
  due_date    DATE,
  amount      NUMERIC(15,2),
  paid        BOOLEAN DEFAULT FALSE,
  created_at  TIMESTAMP DEFAULT NOW()
);

Запуск приложения

go run ./cmd/main.go

После старта приложение будет доступно по адресу http://localhost:8080.

⸻

Доступные маршруты

Открытые (без авторизации)

Метод	Путь	Назначение
POST	/register	Регистрация
POST	/login	Вход в систему
GET	/ping	Проверка доступности

Защищённые маршруты (начинаются с /api)

Метод	Путь	Назначение
GET	/api/me	Получение информации о пользователе
POST	/api/accounts	Создание банковского счёта
GET	/api/accounts	Список счетов пользователя
POST	/api/accounts/deposit	Пополнение счёта
POST	/api/accounts/withdraw	Списание со счёта
POST	/api/transfer	Перевод между счетами
POST	/api/cards	Генерация виртуальной карты
GET	/api/cards	Просмотр всех карт
POST	/api/credits	Оформление кредита
GET	/api/credits/{id}/schedule	График кредитных платежей
GET	/api/accounts/{id}/predict	Прогноз остатка по счёту
GET	/api/analytics/credit-load	Оценка кредитной нагрузки
GET	/api/test-email	Тестовая отправка email
GET	/api/test-rate	Получение ставки ЦБ по SOAP



⸻

HTML-формы для ручного тестирования (встроены в проект)

Путь	Назначение
/register-form	Регистрация
/login-form	Вход в систему
/me-form	Получение текущего пользователя
/accounts-form	Создание счёта
/accounts-balance	Пополнение / снятие
/transfer-form	Перевод средств
/cards-form	Выпуск карты
/cards-view	Список карт
/credits-form	Подать заявку на кредит
/schedule-form	Просмотр графика платежей
/analytics-monthly	Месячная аналитика
/analytics-credit	Кредитная нагрузка
/predict-form	Прогноз остатка
/test-email-form	Проверка email-уведомлений
/test-rate-form	Запрос к Центробанку


