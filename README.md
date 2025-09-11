О проекте
Ads Application — серверная часть сервиса объявлений.
Проект реализует REST‑API, описанное спецификацией OpenAPI, и предоставляет функционал регистрации пользователей, публикации объявлений, добавления комментариев и управления профилем.

Стек технологий
Java 11

Spring Boot 2.7.15 (Web, Data JPA, Security)

PostgreSQL

SpringDoc OpenAPI

Lombok

H2 (тестовая база)

Maven

Запуск
# Сборка проекта
./mvnw clean package

# Запуск приложения
./mvnw spring-boot:run
После старта приложение доступно по адресу http://localhost:8080.
Документация API доступна по пути /swagger-ui.html.

Структура
controller — REST‑контроллеры

dto — объекты передачи данных

mapper — преобразование сущностей в DTO

service и service/impl — бизнес‑логика

repository — работа с базой данных

security — конфигурация безопасности

Участники команды
Codex

insaecula

sazonovfm

skypro-backend
