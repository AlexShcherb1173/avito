# Avito-like Platform

## 📖 Описание проекта
Бэкенд-приложение для доски объявлений (аналог Avito). Позволяет пользователям размещать объявления, комментировать их, управлять профилем.

## 👥 Команда разработчиков
- Задорожный Максим Петрович

## 🛠 Технологии и библиотеки
- **Java 11+**
- **Spring Boot** (Web, Security, Data JPA)
- **PostgreSQL** - реляционная база данных
- **Liquibase** - управление миграциями БД
- **MapStruct** - маппинг объектов
- **Spring Security** - аутентификация и авторизация
- **Swagger/OpenAPI** - документация API
- **Lombok** - сокращение кода
- **Maven** - сборка проекта

## 🚀 Запуск проекта

### Предварительные требования:
1. Установить Java 11+
2. Установить PostgreSQL
3. Создать базу данных `avito_db`

### Настройка базы данных:
```sql
CREATE DATABASE avito_db;
CREATE USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE avito_db TO postgres;
```

### Запуск:
1. Клонировать репозиторий
2. Настроить `application.properties` (указать данные БД)
3. Запустить приложение:
```bash
mvn spring-boot:run
```

### Тестовые пользователи:
- **Админ:** admin@gmail.com / пароль: password123
- **Пользователь:** user@gmail.com / пароль: password123

## 📚 API Документация
После запуска приложения документация доступна по адресу:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI спецификация: http://localhost:8080/v3/api-docs

## 📁 Структура проекта
```
src/main/java/ru/skypro/homework/
├── config/          # Конфигурационные классы
├── controller/      # REST контроллеры
├── dto/            # Data Transfer Objects
├── entity/         # JPA сущности
├── handler/        # Обработчики исключений
├── mapper/         # MapStruct мапперы
├── repository/     # Spring Data репозитории
└── service/        # Бизнес-логика
```

## 🔐 Роли и доступы
- **USER**: Может создавать/редактировать/удалять свои объявления и комментарии
- **ADMIN**: Полный доступ ко всем функциям системы

## 📞 Контакты
ange_fuck@icloud.com