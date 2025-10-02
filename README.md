# 📦 Платформа перепродажи вещей

Дипломный проект по курсу **Java-разработчик (SkyPro)**.  
Бэкенд для площадки по перепродаже вещей.  
Фронтенд предоставлен в рамках задания, реализуется **бэкенд-часть**.

---

## 👩‍💻 Автор
**Юмжана Доржиева**

---

## 📌 Статус проекта
- ✅ Этап I — реализованы DTO и контроллеры (возвращают заглушки).
- ✅ Этап II — добавлены сущности, репозитории, мапперы.
- ⏳ Этап III — сервисный слой и бизнес-логика.
- ⏳ Этап IV — интеграционные тесты и финальная защита.

---

## ⚙️ Технологии
- Java 11
- Spring Boot 2.7
- Spring Web, Spring Security, Spring Data JPA
- PostgreSQL, H2 (для тестов)
- Maven
- Liquibase (SQL-миграции)
- Swagger / OpenAPI

---

## 🗄️ Настройка базы данных
Используется **PostgreSQL**.  
Создать БД и пользователя (например, через **pgAdmin** или psql):

```sql
CREATE DATABASE ads_db;
CREATE USER ads_user WITH PASSWORD 'ads_pass';
GRANT ALL PRIVILEGES ON DATABASE ads_db TO ads_user;
```

---

## 🔄 Liquibase
Миграции базы данных выполняются через **Liquibase**.  
Основной changelog:
```
src/main/resources/db/changelog/db.changelog-master.xml
```

В проекте используется **SQL-формат миграций** (`.sql`), пример:

```sql
-- db/changelog/changeset/create_user_table.sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL
);
```

Файл `db.changelog-master.xml` подключает SQL-скрипты:

```xml
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd">

    <include file="db/changelog/changeset/create_user_table.sql" relativeToChangelogFile="true"/>
</databaseChangeLog>
```

---

## ▶️ Запуск проекта
### 1. Установить зависимости
```bash
mvn clean install
```

### 2. Запустить Spring Boot приложение
```bash
mvn spring-boot:run
```
или через **IntelliJ IDEA** → `Run AdsApplication`.

Приложение поднимется на порту **8080**:  
[http://localhost:8080](http://localhost:8080)

---

## 🔎 API документация
Swagger UI доступен по адресу:  
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🧪 Проверка эндпоинтов
Примеры запросов (через Swagger или Postman):

- `POST /auth/login`
- `POST /auth/register`
- `GET /users/me`
- `POST /users/set_password`
- `PATCH /users/me`
- `GET /ads`
- `POST /ads`
- `GET /ads/{id}`
- `PATCH /ads/{id}`
- `DELETE /ads/{id}`
- `GET /ads/{adId}/comments`
- `POST /ads/{adId}/comments`
- `PATCH /ads/{adId}/comments/{commentId}`
- `DELETE /ads/{adId}/comments/{commentId}`

---

## 📝 Примечания
- На текущем этапе реализованы **DTO, контроллеры и сущности**.
- Liquibase миграции создают таблицы в PostgreSQL.
- Реализация сервисов, репозиториев и тестов будет добавляться на следующих этапах.