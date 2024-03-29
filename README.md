# <span style="color: white">**JAVA-ShareIt**
____
![JAVA-ShareIt](/ShareIt.jpg)

### <span style="color: white"> Данное приложение позволяет брать/давать в аренду вещи другим пользователям, а так же создавать запросы на аренду вещей, которые не представлены на сервисе.
<span style="color: white">Приложение использует слудющие технологии:
- Java version 11
- Spring Boot (web, validation, data-jpa)
- Maven
- используемая БД - PostgreSQL
- Docker
- Lombok
- Json-Simple
- JUnit и Mock тестирование

<span style="color: white">Приложение разделено на 2 сервера, общающихся между собой локально поседствам API:
- gateway (обрабатывает запросы от пользователя, прводит валидацию входных параметров и отправлет HTTP запрсна оновой сервис)
- server  (оснонойсервис с бизнес логикой и собщением с БД)

<span style="color: white">Инструкция по развертыванию приложения:

Так как проект разработан для развертывания на удаленном сервере, запуск его настроен через Docker.
В файле docker-compose.yml прописаны инструкции для приложения по развертыванию и запуску как серверов,
так и необходим БД к ним. Перед запуском убедится что Maven собрал необходимые компоненты, если нет, то произвести
clean - package. Дале сформировать Image в Docker, можно как при помощи средств IDEA (перейти в файл docker-compose.yml
и запустить) или через командную строку, переместившись в нужную директорию и выполнить команды docker-compose build и
docker-compose up. После чего будут собраны Image, на их основе созданы и запущены соответствующие контейнеры.

<span style="color: white">___Ниже приведены эндпоинты и кратное описаних их функционала:___

### <span style="color: white">Users:

* POST /users - создание пользователя
* PATCH /users - редактирование пользователя
* GET /users - получение списка всех пользователей
* GET /users/{userId} - получение данных о пользователе по id
* DELETE /users/{userId} — удаление пользователя

### <span style="color: white">Items:

* POST /items - создание вещи у пользователя
* PATCH /items/{itemId} - редактирование вещи пользователя
* GET /items - получение списка всех вещей пользователя
* GET /items/{itemId} - получение информации о вещи по ее id у пользователя
* GET /items/search?text={text} — поиск вещи по ее названию или описанию

### <span style="color: white">Bookings:

* POST /bookings - создание бронирования
* PATCH /bookings/{bookingId} - редактирование бронирования, подтверждение бронирования для владельца вещи
* GET /bookings/{bookingId} - получение данных о бронировании по id
* GET /bookings - получение списка всех бронирований текущего пользователя
* GET /bookings/owner - получение списка всех бронирований от владельца

### <span style="color: white">Requests:

* POST /requests - создание запроса на аренду вещи с ее поисание
* GET /requests - получение списка запросов на аренду вещей от текущего пользователя 
* GET /requests/all - получение списка запросов на аренду вещей от других пользователей
* GET /requests/{requestId} - получение данных об конкретном запросе на аренду вещи

<span style="color: white">Помимо юнит тестов, в корневой папке проекта имееста Postman коллекция.txt



















