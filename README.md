# Дипломный проект
## Платформа по перепродаже вещей
***

### Проект написан:
* Авдеев Виталий ([GitHub](https://github.com/Vitaliy-Avdeev/A-platform-for-reselling-things))
* ***

***Задача написать бэкенд-часть проекта предполагаемую реализацию следующего функционала:***
* Авторизация и аутентификация пользователей.
* Распределение ролей между пользователями: пользователь и администратор.
* CRUD для объявлений на сайте: администратор может удалять или редактировать все объявления, 
* а пользователи— только свои.
* Под каждым объявлением пользователи могут оставлять отзывы.
* В заголовке сайта можно осуществлять поиск объявлений по названию.
* Показывать и сохранять картинки объявлений.
*

***
### В проекте используются:

* Backend:
    - Java 11
    - Maven
    - Spring Boot
    - Spring Web
    - Spring Data
    - Spring JPA
    - Spring Security
    - GIT
    - REST
    - Swagger
    - Lombok
* SQL:
    - PostgreSQL
    - Liquibase
* Frontend:
    - Docker образ

***

**Для запуска нужно:**
- Клонировать проект в среду разработки
- Прописать properties в файле **[application.properties](src/main/resources/application.properties)**
- Запустить **[Docker](https://www.docker.com)**
- Запустить **Docker образ** (docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21)
- Запустить метод **main** в файле **[HomeworkApplication.java](src/main/java/ru/skypro/homework/HomeworkApplication.java)**

После выполнения всех действий сайт будет доступен по ссылке **http://localhost:3000**

***