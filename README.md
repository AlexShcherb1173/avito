# First Team Graduate Work
## Запуск проекта
Для работы проекта необходимо установить Docker и использовать контейнер
[frontend-react-avito](https://github.com/dmitry-bizin/front-react-avito/tree/v1.21):
```
docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21
```
Либо можно протестировать его с помощью Swagger или Postman по следующему [контракту](https://github.com/dmitry-bizin/front-react-avito/blob/v1.21/openapi.yaml).
## Бэкенд

* Авторизация и аутентификация пользователей.
* Распределение ролей между пользователями: пользователь и администратор.
* CRUD-операции для объявлений и комментариев: администратор может удалять или редактировать все объявления и комментарии, а пользователи — только свои.
* Возможность для пользователей оставлять комментарии под каждым объявлением.
* Показ и сохранение картинок объявлений, а также аватарок пользователей.

Над проектом работали:
* [Юрий Смирнов](https://github.com/jonathan-sm)
* [Павел Фомченков](https://github.com/Pavel-Fomchenkov)
* [Владислав Волков](https://github.com/serper35)
* [Владислав Алиев](https://github.com/VGAliyev)

При разработке проекта использовались следующие инструменты и технологии (библиотеки):
- IntelliJ IDEA
- Java 17
- [Java Telegram Bot API (версия 7.1.0)](https://github.com/pengrad/java-telegram-bot-api)
- SpringBoot (версия 3.3.0)
- Swagger
- Liquibase
- Postgresql
- Docker (запуск фронтенда)
- Сборка с помощью Maven