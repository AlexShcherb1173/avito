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
- ⏳ Этап II — сущности, репозитории, мапперы.
- ⏳ Этап III — сервисный слой и бизнес-логика.
- ⏳ Этап IV — интеграционные тесты и финальная защита.

---

## ⚙️ Технологии
- Java 11
- Spring Boot 2.7
- Spring Web, Spring Security, Spring Data JPA
- PostgreSQL, H2 (для тестов)
- Maven
- Swagger / OpenAPI

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

На этом этапе методы возвращают пустые объекты DTO или `void`.

---

## 📝 Примечания
- На первом этапе реализованы **DTO и контроллеры** согласно OpenAPI-спецификации.
- Реализация сервисов, репозиториев и тестов будет добавляться на следующих этапах.