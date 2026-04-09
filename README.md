# 🚀 Avito Fullstack Application

<p align="center">
  Fullstack marketplace (React + Spring Boot + PostgreSQL)
</p>

---

## 📌 Badges

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring](https://img.shields.io/badge/SpringBoot-2.7-green)
![React](https://img.shields.io/badge/React-18-blue)
![Redux](https://img.shields.io/badge/Redux-4-purple)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![Docker](https://img.shields.io/badge/Docker-ready-blue)
![Tests](https://img.shields.io/badge/coverage-90%25-brightgreen)

---

## 📖 Описание проекта

**Avito** — это fullstack веб-приложение маркетплейса.

### Возможности:
- 🔐 регистрация и авторизация
- 📦 создание объявлений (с изображениями)
- ✏️ редактирование и удаление объявлений
- 👤 профиль пользователя
- 💬 комментарии к объявлениям
- 🖼 загрузка изображений
- 📡 REST API

---

## 🧱 Архитектура

```mermaid
flowchart LR
    U[User] --> F[React Frontend]
    F -->|REST API| B[Spring Boot Backend]
    B --> DB[(PostgreSQL)]
    B --> FS[/Image Storage]
    B --> Flyway[Flyway Migrations]
🖥 Frontend
Стек:
React 18
Redux
React Router
Webpack
Sass
Особенности:
SPA архитектура
централизованное состояние (Redux)
кастомные hooks
API слой (utils/api.js)
📊 Покрытие тестами
Метрика	Значение
Statements	91%
Branches	77%
Functions	89%
Lines	91%
⚙️ Backend
Стек:
Java 17
Spring Boot
Spring Security
JPA / Hibernate
PostgreSQL
Flyway
Возможности:
CRUD объявлений
комментарии
управление пользователем
загрузка файлов
безопасность
📊 Покрытие тестами
Слой	Coverage
Controllers	100%
Services	100%
Impl	~92%
Total	~76%
📂 Структура проекта
avito/
├── backend/
│   ├── controller/
│   ├── service/
│   ├── entity/
│   ├── dto/
│   ├── security/
│   └── config/
│
├── frontend/
│   ├── components/
│   ├── redux/
│   ├── utils/
│   └── hooks/
│
├── docker-compose.yml
└── README.md
Архитектура backend (детально)
![](F:\dev docs\java avito3 - Покрытие тестов фронта.html)
Скриншоты
![Главная](docs/screens/main.png)
![Профиль](docs/screens/profile.png)
![Объявление](docs/screens/ad.png)
Быстрый запуск
🐳 Через Docker (рекомендуется)
1. Создать .env
POSTGRES_DB=avito_db
POSTGRES_USER=avito_user
POSTGRES_PASSWORD=avito_password

SPRING_DATASOURCE_URL=jdbc:postgresql://avito-db:5432/avito_db
SPRING_DATASOURCE_USERNAME=avito_user
SPRING_DATASOURCE_PASSWORD=avito_password

SPRING_PROFILES_ACTIVE=docker
APP_IMAGES_DIR=/images
2. Запуск
docker compose up --build
3. Открыть
Frontend → http://localhost:3001
Backend → http://localhost:8081
docker compose up --build
cd backend
mvn clean spring-boot:run
Frontend
cd frontend
npm install
npm run dev
Деплой (VPS)
git clone https://github.com/AlexShcherb1173/avito.git
cd avito
docker compose up -d --build
API

Swagger:
http://localhost:8081/swagger-ui/
Тестирование
Frontend
npm run test
npm run test:coverage
Backend
mvn clean test
Docker
Backend
multi-stage build
Maven → JAR → runtime
Frontend
build через webpack
nginx как web server
🔥 Что демонстрирует проект
fullstack архитектуру
REST API
работу с файлами
безопасность (Spring Security)
state management (Redux)
тестирование (Jest + JUnit)
Docker orchestration
👨‍💻 Автор

Alex Shcherb

GitHub: https://github.com/AlexShcherb1173
⭐ Итог

Проект уровня:

👉 Junior+ / Middle Fullstack
👉 Готов для портфолио
👉 Можно масштабировать до production