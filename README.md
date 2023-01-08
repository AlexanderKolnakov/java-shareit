# java-filmorate

Бэкенд web-сервиса, который будет работать с вещами, что пользователи хотят сдать или взять в аренду.

### Реализованы следующие эндпоинты:

#### 1. Пользователи

* POST /users - создание пользователя
* PATCH /users - редактирование пользователя
* GET /users - получение списка всех пользователей
* GET /users/{userId} - получение данных о пользователе по id
* DELETE /users/{userId} — удаление пользователя

#### 2. Вещи

* POST /items - создание вещи у пользователя
* PATCH /items/{itemId} - редактирование вещи пользователя
* GET /items - получение списка всех вещей пользователя
* GET /items/{itemId} - получение информации о вещи по ее id у пользователя
* GET /items/search?text={text} — поиск вещи по ее названию или описанию

#### 3. Бронирование (booking)

* POST /bookings - создание бронирования
* PATCH /bookings - редактирование бронирования, подтверждение бронирования для владельца вещи
* GET /bookings - получение списка всех бронирований (/owner - для владельца вещи)
*
    + поиск по статусу бронирования (по умолчанию статус ALL)
* GET /bookings/{userId} - получение данных о бронировании по id

Данные хранятся в БД. Схема БД представлена в фале schema.sql.

*Дата создания первой версии: 22.11.2022* <br>
*Дата последнего обновления: 08.01.2023*
