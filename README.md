# Игра на Kotlin
Карточная игра, где несколько игроков собирают очки, разыгрывая специальные карты

## Как играть

Запустите базу данных [docker-compose.yaml](docker-compose.yaml)

Запустите [GameApplication.kt](src%2Fmain%2Fkotlin%2FGameApplication.kt)

Создастся база данных и добавятся шаблоны для карт. Карты генерируются рандомно.

Авторизация и аутентификация происходят с помощью jwt-Токена

## Эндпоинты:

http://localhost:8080/auth/register?name=name&password=pass - регистрация пользователя
(замените name и pass на ваши данные)

http://localhost:8080/auth/login?name=name&password=pass - вход уже зарегистрированного пользователя

Далее идут эндпоинты с bearer-token

http://localhost:8080/game/create создание игры, возвращает id игры

http://localhost:8080/game/{gameId}/join присоединение к игре, возвращает количество игроков в партии

http://localhost:8080/game/{gameId}/start начало игры

http://localhost:8080/game/{gameId}/play ходы игроков

http://localhost:8080/game/{gameId}/steal-points/from-player/{victimUserId} Использование карты "steal" для кражи очков у другого игрока
( замените {victimUserId} на ID игрока, у которого хотите украсть очки)

