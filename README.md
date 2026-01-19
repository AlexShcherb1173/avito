Материалы для выполнения курсовой работы учениками профессии java-разработчик.

# 🏠 Ads Platform - Дипломный проект

Платформа для размещения объявлений с комментариями и изображениями.

## 📋 Оглавление
- [Технологический стек](#технологический-стек)
- [Архитектура](#архитектура)
- [Архитектура](#архитектура) 
- [Установка и запуск](#установка-и-запуск)
- [API Endpoints](#api-endpoints)
- [База данных](#база-данных)
- [Примеры использования](#примеры-использования)
- [Схемы взаимодействия](#схемы-взаимодействия)
- [Безопасность](#безопасность)
- [Тестирование](#тестирование)
- [Деплой](#деплой)
- [Мониторинг](#мониторинг)
- [Вклад в проект](#вклад-в-проект)

## 🛠 Технологический стек

### Backend
- **Java 17** - основной язык разработки
- **Spring Boot 2.7.15** - фреймворк
- **Spring Security** - аутентификация и авторизация
- **Spring Data JPA** - работа с базой данных
- **PostgreSQL** - основная база данных
- **H2 Database** - тестовая база данных (для разработки)
- **Hibernate** - ORM

### Frontend
- **React** (предположительно, судя по CORS настройкам)
- Работает на порту 3000

### Инструменты
- **Maven** - сборка проекта
- **Lombok** - уменьшение boilerplate кода
- **MapStruct** - маппинг DTO
- **SLF4J/Logback** - логирование

## 🏗 Архитектура

### Структура проекта
```
src/main/java/ru/skypro/homework/
├── controller/          # REST контроллеры
│   ├── AdsController.java
│   ├── AuthController.java
│   └── UserController.java
├── dto/                # Data Transfer Objects
│   ├── ad/
│   ├── comment/
│   └── user/
├── entity/             # JPA сущности
│   ├── Ad.java
│   ├── Comment.java
│   └── User.java
├── repository/         # Spring Data репозитории
├── service/            # Бизнес-логика
│   ├── AdService.java
│   ├── CommentService.java
│   └── UserService.java
├── mapper/             # MapStruct мапперы
├── config/             # Конфигурационные классы
├── security/           # Spring Security настройки
└── HomeworkApplication.java
```

### Принципы проектирования
- **RESTful API** - соблюдение REST принципов
- **Слоистая архитектура** (Controller-Service-Repository)
- **DTO Pattern** - отделение внутренней модели от API
- **Dependency Injection** - через Spring

## 🚀 Установка и запуск

### Требования
- Java 17+
- PostgreSQL 14+
- Maven 3.6+
- Node.js 16+ (для фронтенда)

### 1. Настройка базы данных
```sql
-- Создание базы данных
CREATE DATABASE ads_db;

-- Создание пользователя (опционально)
CREATE USER app_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE ads_db TO app_user;
```

### 2. Конфигурация приложения
Создайте `application.properties` в `src/main/resources/`:
```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ads_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# CORS для фронтенда
cors.allowed-origins=http://localhost:3000

# Security (для разработки)
security.enabled=true
```

### 3. Сборка и запуск
```bash
# Сборка проекта
mvn clean package

# Запуск приложения
java -jar target/graduate_work-1.0-SNAPSHOT.jar

# Или запуск через Maven
mvn spring-boot:run
```

### 4. Для разработки с H2
```properties
# Временная конфигурация для тестирования
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## 📡 API Endpoints

### Аутентификация
| Метод | Endpoint | Описание | Аутентификация |
|-------|----------|----------|----------------|
| POST | `/api/login` | Вход в систему | Не требуется |
| POST | `/api/register` | Регистрация | Не требуется |

### Объявления (Ads)
| Метод | Endpoint | Описание | Аутентификация |
|-------|----------|----------|----------------|
| GET | `/api/ads` | Получить все объявления | Не требуется |
| POST | `/api/ads` | Создать объявление | Требуется |
| GET | `/api/ads/{id}` | Получить объявление по ID | Не требуется |
| DELETE | `/api/ads/{id}` | Удалить объявление | Требуется (владелец/админ) |
| PATCH | `/api/ads/{id}` | Обновить объявление | Требуется (владелец/админ) |
| GET | `/api/ads/me` | Мои объявления | Требуется |

### Комментарии
| Метод | Endpoint | Описание | Аутентификация |
|-------|----------|----------|----------------|
| GET | `/api/ads/{id}/comments` | Комментарии объявления | Не требуется |
| POST | `/api/ads/{id}/comments` | Добавить комментарий | Требуется |
| DELETE | `/api/ads/{adId}/comments/{commentId}` | Удалить комментарий | Требуется (владелец/админ) |
| PATCH | `/api/ads/{adId}/comments/{commentId}` | Обновить комментарий | Требуется (владелец/админ) |

### Пользователи
| Метод | Endpoint | Описание | Аутентификация |
|-------|----------|----------|----------------|
| GET | `/api/users/me` | Получить свой профиль | Требуется |
| PATCH | `/api/users/me` | Обновить профиль | Требуется |
| PATCH | `/api/users/me/image` | Обновить аватар | Требуется |
| GET | `/api/users/{id}/image` | Получить аватар | Не требуется |

### Изображения
| Метод | Endpoint | Описание | Аутентификация |
|-------|----------|----------|----------------|
| PATCH | `/api/ads/{id}/image` | Обновить изображение объявления | Требуется (владелец/админ) |
| GET | `/api/ads/image/{id}` | Получить изображение объявления | Не требуется |

## 🗄 База данных

### ER-диаграмма
```plantuml
@startuml
!define Table(name,desc) class name as "desc" << (T,#FFAAAA) >>
!define PrimaryKey(x) <b><color:red>#x#</color></b>
!define ForeignKey(x) <color:blue>#x#</color>

Table(User, "users") {
  PrimaryKey(id): INTEGER
  --
  username: VARCHAR(255) UNIQUE
  password: VARCHAR(255)
  first_name: VARCHAR(255)
  last_name: VARCHAR(255)
  phone: VARCHAR(255)
  role: VARCHAR(255)
  enabled: BOOLEAN
  image: VARCHAR(255)
}

Table(Ad, "ads") {
  PrimaryKey(pk): INTEGER
  --
  title: VARCHAR(255)
  description: VARCHAR(255)
  price: INTEGER
  ForeignKey(author_id): INTEGER
  image: VARCHAR(255)
  created_at: TIMESTAMP
}

Table(Comment, "comments") {
  PrimaryKey(pk): INTEGER
  --
  text: TEXT
  ForeignKey(author_id): INTEGER
  ForeignKey(ad_id): INTEGER
  created_at: TIMESTAMP
}

User "1" -- "*" Ad : "author"
User "1" -- "*" Comment : "author"
Ad "1" -- "*" Comment : "comments"
@enduml
```

### Создание таблиц
```sql
-- Пользователи
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    image VARCHAR(255)
);

-- Объявления
CREATE TABLE ads (
    pk SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price INTEGER NOT NULL,
    author_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Комментарии
CREATE TABLE comments (
    pk SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    author_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ad_id INTEGER NOT NULL REFERENCES ads(pk) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 📊 Примеры использования

### Регистрация пользователя
```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123",
    "firstName": "Иван",
    "lastName": "Иванов",
    "phone": "+79998887766",
    "role": "USER"
  }'
```

### Создание объявления
```bash
curl -X POST http://localhost:8080/api/ads \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: multipart/form-data" \
  -F "properties={\"title\":\"Продам велосипед\",\"description\":\"Отличный горный велосипед\",\"price\":15000};type=application/json" \
  -F "image=@bicycle.jpg"
```

### Получение всех объявлений
```bash
curl -X GET http://localhost:8080/api/ads
```

## 🔄 Схемы взаимодействия

### 1. Создание объявления с комментарием
```plantuml
@startuml
title Sequence Diagram: Создание объявления и добавление комментария

actor User as U
participant Frontend as FE
participant AdsController as AC
participant AdService as AS
participant CommentService as CS
database PostgreSQL as DB

== Регистрация/Аутентификация ==
U -> FE: Заполняет форму регистрации
FE -> AC: POST /api/register
AC -> DB: Сохраняет пользователя
DB -> AC: Возвращает ID
AC -> FE: 201 CREATED с JWT токеном
FE -> U: Сохраняет токен

== Создание объявления ==
U -> FE: Заполняет форму объявления + загружает фото
FE -> AC: POST /api/ads (multipart/form-data)
AC -> AC: Валидация, извлечение JWT из заголовка
AC -> AS: addAd(properties, image, authentication)
AS -> DB: Сохраняет объявление
DB -> AS: Возвращает ID созданного объявления
AS -> AC: Объект AdDTO
AC -> FE: 201 CREATED с AdDTO
FE -> U: Показывает созданное объявление

== Добавление комментария ==
U -> FE: Пишет комментарий
FE -> AC: POST /api/ads/{id}/comments
AC -> CS: addComment(id, commentDTO, authentication)
CS -> DB: Проверяет существование объявления
CS -> DB: Сохраняет комментарий
DB -> CS: ID комментария
CS -> AC: CommentDTO
AC -> FE: 200 OK с CommentDTO
FE -> U: Обновляет список комментариев

@enduml
```

### 2. Полный жизненный цикл объявления
```plantuml
@startuml
title Sequence Diagram: Жизненный цикл объявления

actor User as U
participant Frontend as FE
participant AdsController as AC
participant AdService as AS
participant CommentService as CS
participant ImageService as IS
database PostgreSQL as DB
database FileStorage as FS

group Создание и модификация
    U -> FE: Создает объявление с изображением
    FE -> AC: POST /api/ads
    AC -> AS: addAd()
    AS -> FS: Сохраняет изображение
    FS -> AS: Возвращает путь к файлу
    AS -> DB: Сохраняет объявление
    DB -> AS: ID объявления
    AS -> AC: AdDTO
    AC -> FE: 201 CREATED
    
    U -> FE: Редактирует объявление
    FE -> AC: PATCH /api/ads/{id}
    AC -> AS: updateAd()
    AS -> DB: Обновляет данные
    DB -> AS: Обновленная запись
    AS -> AC: Обновленный AdDTO
    AC -> FE: 200 OK
    
    U -> FE: Загружает новое изображение
    FE -> AC: PATCH /api/ads/{id}/image
    AC -> IS: updateAdImage()
    IS -> FS: Удаляет старое изображение
    IS -> FS: Сохраняет новое изображение
    IS -> DB: Обновляет путь к изображению
    IS -> AC: byte[] изображения
    AC -> FE: 200 OK
end

group Взаимодействие с комментариями
    U -> FE: Просматривает объявление
    FE -> AC: GET /api/ads/{id}
    AC -> AS: getExtendedAd()
    AS -> DB: Получает объявление + автора
    AS -> AC: ExtendedAdDTO
    AC -> FE: 200 OK
    
    FE -> AC: GET /api/ads/{id}/comments
    AC -> CS: getComments()
    CS -> DB: Получает комментарии + авторов
    CS -> AC: CommentsDTO
    AC -> FE: 200 OK
end

group Удаление
    U -> FE: Удаляет объявление
    FE -> AC: DELETE /api/ads/{id}
    AC -> AS: removeAd()
    AS -> CS: Удалить все комментарии (каскадно)
    AS -> IS: Удалить изображение
    IS -> FS: Удаляет файл изображения
    AS -> DB: Удаляет объявление
    AC -> FE: 204 NO CONTENT
end

@enduml
```

### 3. Работа с изображениями
```plantuml
@startuml
title Sequence Diagram: Работа с изображениями

actor Frontend as FE
participant AdsController as AC
participant AdService as AS
participant UserService as US
database PostgreSQL as DB
database FileStorage as FS

== Загрузка изображения объявления ==

FE -> AC: POST /api/ads (с image в multipart)
AC -> AS: addAd(properties, image, auth)
AS -> AS: Извлекает bytes из MultipartFile
AS -> FS: Сохраняет изображение (generate filename)
FS -> AS: Возвращает путь/имя файла
AS -> DB: Сохраняет объявление с путем к изображению
DB -> AS: ID объявления
AS -> AC: AdDTO с image path
AC -> FE: 201 CREATED

== Получение изображения ==

FE -> AC: GET /api/ads/image/{id}
AC -> AS: getAdImage(id)
AS -> DB: Получает информацию об объявлении
AS -> FS: Запрашивает файл по пути
FS -> AS: byte[] изображения
AS -> AC: byte[]
AC -> FE: 200 OK с Content-Type: image/*

== Обновление аватара пользователя ==

FE -> AC: PATCH /api/users/me/image (multipart)
AC -> US: updateUserImage(image, auth)
US -> DB: Получает текущего пользователя
US -> FS: Удаляет старый аватар (если есть)
US -> FS: Сохраняет новый аватар
FS -> US: Новый путь к файлу
US -> DB: Обновляет imagePath у пользователя
US -> AC: byte[] нового аватара
AC -> FE: 200 OK

@enduml
```

### 4. Процесс аутентификации и авторизации
```plantuml
@startuml
title Sequence Diagram: Аутентификация и доступ к ресурсам

actor User as U
participant Frontend as FE
participant AuthController as AC
participant UserController as UC
participant SpringSecurity as SS
database PostgreSQL as DB

== Регистрация ==
U -> FE: Заполняет форму регистрации
FE -> AC: POST /api/register (userDTO)
AC -> DB: Проверяет уникальность username
AC -> DB: Сохраняет пользователя (с хешированием пароля)
DB -> AC: ID пользователя
AC -> FE: 201 CREATED (без пароля)

== Аутентификация ==
U -> FE: Вводит credentials
FE -> AC: POST /api/login (username, password)
AC -> SS: authenticate(username, password)
SS -> DB: Проверяет credentials
SS -> SS: Генерирует JWT токен
SS -> AC: Authentication object
AC -> FE: 200 OK с JWT токеном
FE -> U: Сохраняет токен в localStorage

== Доступ к защищенному ресурсу ==
U -> FE: Запрашивает "Мои объявления"
FE -> UC: GET /api/users/me (с Authorization header)
UC -> SS: Проверяет JWT токен
SS -> SS: Валидирует токен, извлекает данные пользователя
SS -> UC: Authentication object
UC -> DB: Запрашивает данные пользователя по username из токена
DB -> UC: Данные пользователя
UC -> FE: 200 OK с UserDTO

== Проверка прав доступа ==
U -> FE: Пытается удалить чужое объявление
FE -> AdsController: DELETE /api/ads/{id} (с токеном)
AdsController -> SS: Проверяет аутентификацию
SS -> AdsController: Authentication object
AdsController -> DB: Получает объявление, проверяет author_id
DB -> AdsController: Данные объявления
AdsController -> SS: Проверяет роль (ADMIN) или совпадение author_id
SS -> AdsController: Access Denied (403)
AdsController -> FE: 403 FORBIDDEN
FE -> U: Показывает ошибку "Нет прав"
@enduml
```

## 🔒 Безопасность

### Роли пользователей
- **USER** - обычный пользователь (может создавать объявления, комментировать)
- **ADMIN** - администратор (все права + управление пользователями)

### Защита endpoints
- **Публичные**: GET /api/ads, GET /api/ads/{id}, GET /api/ads/{id}/comments
- **Требуют аутентификации**: POST/PATCH/DELETE операции
- **Проверка владения**: пользователь может изменять только свои ресурсы

### JWT токены
- Используются для stateless аутентификации
- Время жизни: 24 часа
- Хранятся в localStorage фронтенда

## 🧪 Тестирование

### Запуск тестов
```bash
# Все тесты
mvn test

# С покрытием кода
mvn jacoco:report
```

### Примеры тестов
- **Unit тесты** сервисов
- **Integration тесты** контроллеров
- **Тесты безопасности** (роли и доступ)

## 🚢 Деплой

### Docker контейнеризация
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/graduate_work-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: ads_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
  
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/ads_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: password
    depends_on:
      - postgres

volumes:
  postgres_data:
```

## 📈 Мониторинг

### Health checks
```bash
# Проверка здоровья приложения
curl http://localhost:8080/api/actuator/health

# Информация о приложении
curl http://localhost:8080/api/actuator/info
```

### Логирование
- Уровень логирования настраивается в `application.properties`
- Логи хранятся в файлах с ротацией по размеру/времени

## 🤝 Вклад в проект

### Code style
- Java Code Conventions
- Использование Lombok для геттеров/сеттеров
- MapStruct для маппинга DTO

### Git workflow
1. Создать feature branch от `main`
2. Реализовать функциональность
3. Написать тесты
4. Создать Pull Request
5. Code review
6. Мердж в `main`

---

