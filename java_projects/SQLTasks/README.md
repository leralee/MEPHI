# DatabasesWorks

Проект для работы с различными базами данных (БД) с целью получения сводных данных. Реализован на MySQL.

## Цели проекта
- Организация отчётности по видам транспорта
- Анализ результатов автомобильных гонок
- Управление данными бронирований отелей
- Мониторинг сотрудников и проектов в организации

## Структура репозитория

```
.
├── vehicles/               # База данных "Виды транспорта"
│   ├── sql1.sql        
│   └── sql2.sql        
│
├── races/                 # База данных "Автогонки"
│   ├── sql1.sql          
│   └── sql2.sql    
│   ├── sql3.sql          
│   └── sql4.sql   
│   ├── sql5.sql          
│
├── bookings/              # База данных "Бронирования"
│   ├── sql1.sql    
│   ├── sql2.sql       
│   └── sql3.sql       
│
├── departments/           # База данных "Отделения"
│   ├── sql1.sql    
│   ├── sql2.sql        
│   └── sql3.sql         
```


## Описание БД
#### 1. **База данных «Виды транспорта» (`vehicles`)**

**Структура:**
- **`Vehicle`** (основная информация):  
  `maker` (производитель), `model` (модель), `type` (тип транспорта: Car/Motorcycle/Bicycle).
- **`Car`** (автомобили):  
  `vin` (идентификатор), `engine_capacity` (объём двигателя), `horsepower` (мощность), `price` (цена), `transmission` (тип КПП).
- **`Motorcycle`** (мотоциклы):  
  Аналогично автомобилям + `type` (Sport/Cruiser/Touring).
- **`Bicycle`** (велосипеды):  
  `serial_number`, `gear_count` (количество передач), `type` (Mountain/Road/Hybrid).

**Связи:**
- `Car.model`, `Motorcycle.model`, `Bicycle.model` → `Vehicle.model`.
---

#### 2. **База данных «Автогонки» (`races`)**

**Структура:**
- **`Classes`** (классы автомобилей):  
  `class` (название), `type` (Racing/Street), `country`, `engineSize`, `weight`.
- **`Cars`** (автомобили):  
  `name`, `class` (ссылка на Classes), `year` (год выпуска).
- **`Races`** (гонки):  
  `name`, `date`.
- **`Results`** (результаты гонок):  
  `car` (ссылка на Cars), `race` (ссылка на Races), `position`.
---

#### 3. **База данных «Бронирования» (`bookings`)**
**Структура:**
- **`Hotel`**: `ID_hotel`, `name`, `location`.
- **`Room`**: `ID_room`, `ID_hotel`, `room_type` (Single/Double/Suite), `price`, `capacity`.
- **`Customer`**: `ID_customer`, `name`, `email`, `phone`.
- **`Booking`**: `ID_booking`, `ID_room`, `ID_customer`, даты заезда/выезда.

---

#### 4. **База данных «Отделения компании» (`departments`)**

**Структура:**
- **`Departments`**: `DepartmentID`, `DepartmentName`.
- **`Roles`**: `RoleID`, `RoleName` (Менеджер, Разработчик и т.д.).
- **`Employees`**: Иерархия сотрудников с ссылкой на менеджера (`ManagerID`), отдел и роль.
- **`Projects`**: Проекты с датами и привязкой к отделам.
- **`Tasks`**: Задачи с назначением на сотрудников и проекты.

---


