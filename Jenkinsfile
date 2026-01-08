pipeline {
    agent any

    parameters {
        string(
            name: 'BRANCH',
            defaultValue: 'main',
            description: 'Ветка для сборки'
        )
        choice(
            name: 'TEST_TYPE',
            choices: ['all', 'api', 'ui', 'integration'],
            description: 'Тип тестов для запуска'
        )
        booleanParam(
            name: 'RUN_DOCKER',
            defaultValue: true,
            description: 'Запускать тесты в Docker-контейнерах'
        )
        booleanParam(
            name: 'CLEANUP_DOCKER',
            defaultValue: true,
            description: 'Очищать Docker после тестов'
        )
    }

    environment {
        PROJECT_NAME = 'avito-qa'
        DOCKER_REGISTRY = 'ghcr.io'
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/dmitry-bizin/front-react-avito"
        DOCKER_TAG = 'v1.21'

        // PostgreSQL для тестов
        POSTGRES_TEST_DB = 'avito_test'
        POSTGRES_TEST_USER = 'postgres'
        POSTGRES_TEST_PASSWORD = 'password'
        POSTGRES_TEST_PORT = '5435'

        // Приложение
        APP_PORT = '8081'
        FRONTEND_PORT = '3001'
        SELENIUM_PORT = '4444'

        // Настройки Maven
        MAVEN_OPTS = '-Xmx2g -XX:MaxPermSize=512m'

        // Allure
        ALLURE_RESULTS = 'target/allure-results'
        ALLURE_REPORT = 'target/allure-report'
    }

    tools {
        maven 'Maven_3.8'
        jdk 'JDK_11'
    }

    stages {
        stage('Подготовка') {
            steps {
                script {
                    echo "Начало сборки проекта ${PROJECT_NAME}"
                    echo "Ветка: ${params.BRANCH}"
                    echo "Тип тестов: ${params.TEST_TYPE}"
                    echo "Запуск в Docker: ${params.RUN_DOCKER}"

                    // Проверка наличия необходимых инструментов
                    sh 'java -version'
                    sh 'mvn --version'
                    sh 'docker --version'
                }

                checkout scm

                // Установка ветки если указана параметром
                script {
                    if (params.BRANCH != '') {
                        sh "git checkout ${params.BRANCH}"
                    }
                }
            }
        }

        stage('Сборка проекта') {
            steps {
                echo 'Сборка проекта Maven...'
                sh """
                    mvn clean compile -DskipTests=true \
                    -Dspring.profiles.active=test \
                    -Pwith-frontend
                """
            }
        }

        stage('API тесты') {
            when {
                anyOf {
                    expression { params.TEST_TYPE == 'all' }
                    expression { params.TEST_TYPE == 'api' }
                    expression { params.TEST_TYPE == 'integration' }
                }
            }
            steps {
                script {
                    echo 'Запуск API тестов...'

                    if (params.RUN_DOCKER) {
                        // Запуск API тестов в Docker
                        runApiTestsInDocker()
                    } else {
                        // Запуск API тестов локально
                        runApiTestsLocally()
                    }
                }
            }
            post {
                always {
                    // Сбор результатов Allure
                    script {
                        if (fileExists("${ALLURE_RESULTS}")) {
                            allure([
                                includeProperties: false,
                                jdk: '',
                                properties: [],
                                reportBuildPolicy: 'ALWAYS',
                                results: [[path: "${ALLURE_RESULTS}"]]
                            ])
                        }
                    }
                }
            }
        }

        stage('UI тесты') {
            when {
                anyOf {
                    expression { params.TEST_TYPE == 'all' }
                    expression { params.TEST_TYPE == 'ui' }
                    expression { params.TEST_TYPE == 'integration' }
                }
            }
            steps {
                script {
                    echo 'Запуск UI тестов...'

                    if (params.RUN_DOCKER) {
                        // Запуск UI тестов в Docker
                        runUiTestsInDocker()
                    } else {
                        // Запуск UI тестов локально
                        runUiTestsLocally()
                    }
                }
            }
            post {
                always {
                    // Сбор результатов Allure для UI тестов
                    script {
                        def uiAllureResults = 'target/ui-allure-results'
                        if (fileExists(uiAllureResults)) {
                            dir(uiAllureResults) {
                                allure([
                                    includeProperties: false,
                                    jdk: '',
                                    properties: [],
                                    reportBuildPolicy: 'ALWAYS',
                                    results: [[path: uiAllureResults]]
                                ])
                            }
                        }
                    }
                }
            }
        }

        stage('Интеграционные тесты') {
            when {
                expression { params.TEST_TYPE == 'integration' }
            }
            steps {
                script {
                    echo 'Запуск интеграционных тестов в Docker Compose...'
                    runIntegrationTests()
                }
            }
        }

        stage('Генерация отчетов') {
            steps {
                script {
                    echo 'Генерация отчетов о тестировании...'

                    // Отчет JaCoCo
                    sh 'mvn jacoco:report'

                    // Отчет SpotBugs/FindBugs если есть
                    sh 'mvn site -DskipTests'

                    // Архивирование отчетов
                    sh """
                        tar -czf test-reports.tar.gz \
                            target/site/ \
                            target/allure-report/ \
                            target/surefire-reports/ \
                            target/failsafe-reports/ \
                            target/jacoco-report/
                    """

                    archiveArtifacts artifacts: 'test-reports.tar.gz', fingerprint: true
                }
            }
        }
    }

    post {
        always {
            script {
                echo 'Очистка окружения...'

                if (params.CLEANUP_DOCKER && params.RUN_DOCKER) {
                    cleanupDocker()
                }

                // Очистка Maven
                sh 'mvn clean -q'

                // Сохранение логов
                archiveArtifacts artifacts: '**/target/*.log', allowEmptyArchive: true
            }
        }
        success {
            echo 'Сборка успешно завершена! ✅'
            emailext(
                subject: "✅ Сборка ${PROJECT_NAME} #${BUILD_NUMBER} успешна",
                body: """
                    Проект: ${PROJECT_NAME}
                    Сборка: #${BUILD_NUMBER}
                    Статус: УСПЕШНО
                    Ветка: ${params.BRANCH}
                    Тип тестов: ${params.TEST_TYPE}
                    Ссылка на сборку: ${BUILD_URL}
                    Все отчеты доступны в Jenkins.
                """,
                to: 'team@example.com',
                attachLog: false
            )
        }
        failure {
            echo 'Сборка завершилась с ошибкой! ❌'
            emailext(
                subject: "❌ Сборка ${PROJECT_NAME} #${BUILD_NUMBER} провалилась",
                body: """
                    Проект: ${PROJECT_NAME}
                    Сборка: #${BUILD_NUMBER}
                    Статус: ПРОВАЛ
                    Ветка: ${params.BRANCH}
                    Тип тестов: ${params.TEST_TYPE}
                    Ссылка на сборку: ${BUILD_URL}
                    Проверьте логи для деталей.
                """,
                to: 'team@example.com',
                attachLog: true
            )
        }
        unstable {
            echo 'Сборка нестабильна (есть упавшие тесты) ⚠️'
        }
    }
}

// Функции для запуска тестов

def runApiTestsInDocker() {
    echo 'Запуск API тестов в Docker контейнере...'

    // Сборка образа приложения для тестов
    sh 'docker build -t avito-app-test:latest .'

    // Запуск PostgreSQL и приложения
    sh """
        docker network create test-network || true

        # Запуск PostgreSQL
        docker run -d \
            --name postgres-test \
            --network test-network \
            -e POSTGRES_DB=${POSTGRES_TEST_DB} \
            -e POSTGRES_USER=${POSTGRES_TEST_USER} \
            -e POSTGRES_PASSWORD=${POSTGRES_TEST_PASSWORD} \
            -p ${POSTGRES_TEST_PORT}:5432 \
            postgres:15

        # Ожидание готовности PostgreSQL
        sleep 10
        docker exec postgres-test pg_isready -U ${POSTGRES_TEST_USER}

        # Запуск приложения
        docker run -d \
            --name app-test \
            --network test-network \
            -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-test:5432/${POSTGRES_TEST_DB} \
            -e SPRING_DATASOURCE_USERNAME=${POSTGRES_TEST_USER} \
            -e SPRING_DATASOURCE_PASSWORD=${POSTGRES_TEST_PASSWORD} \
            -e SPRING_PROFILES_ACTIVE=test \
            -p ${APP_PORT}:8080 \
            avito-app-test:latest

        # Ожидание запуска приложения
        sleep 15
        curl -f http://localhost:${APP_PORT}/actuator/health || echo "Приложение запускается..."
    """

    // Запуск тестов
    try {
        sh """
            mvn test -Dtest="*ApiTest" \
                -Dspring.profiles.active=test \
                -Dserver.port=${APP_PORT} \
                -Dspring.datasource.url=jdbc:postgresql://localhost:${POSTGRES_TEST_PORT}/${POSTGRES_TEST_DB} \
                -Dspring.datasource.username=${POSTGRES_TEST_USER} \
                -Dspring.datasource.password=${POSTGRES_TEST_PASSWORD}
        """
    } finally {
        // Сохранение логов контейнеров
        sh """
            docker logs app-test > target/app-test.log 2>&1 || true
            docker logs postgres-test > target/postgres-test.log 2>&1 || true
        """
    }
}

def runApiTestsLocally() {
    echo 'Запуск API тестов локально...'

    // Запуск PostgreSQL локально через Docker
    sh """
        docker run -d \
            --name local-postgres-test \
            -e POSTGRES_DB=${POSTGRES_TEST_DB} \
            -e POSTGRES_USER=${POSTGRES_TEST_USER} \
            -e POSTGRES_PASSWORD=${POSTGRES_TEST_PASSWORD} \
            -p ${POSTGRES_TEST_PORT}:5432 \
            postgres:15

        sleep 10
        docker exec local-postgres-test pg_isready -U ${POSTGRES_TEST_USER}
    """

    // Запуск тестов
    try {
        sh """
            mvn test -Dtest="*ApiTest" \
                -Dspring.profiles.active=test \
                -Dspring.datasource.url=jdbc:postgresql://localhost:${POSTGRES_TEST_PORT}/${POSTGRES_TEST_DB} \
                -Dspring.datasource.username=${POSTGRES_TEST_USER} \
                -Dspring.datasource.password=${POSTGRES_TEST_PASSWORD}
        """
    } finally {
        sh 'docker logs local-postgres-test > target/postgres-local.log 2>&1 || true'
    }
}

def runUiTestsInDocker() {
    echo 'Запуск UI тестов в Docker Compose...'

    // Создание docker-compose для UI тестов
    writeFile file: 'docker-compose-ui.yml', text: """
version: '3.8'
services:
  postgres-test:
    image: postgres:15
    environment:
      POSTGRES_DB: ${POSTGRES_TEST_DB}
      POSTGRES_USER: ${POSTGRES_TEST_USER}
      POSTGRES_PASSWORD: ${POSTGRES_TEST_PASSWORD}
    ports:
      - "${POSTGRES_TEST_PORT}:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_TEST_USER}"]
      interval: 5s
      timeout: 5s
      retries: 5

  frontend-test:
    image: ${DOCKER_IMAGE}:${DOCKER_TAG}
    ports:
      - "${FRONTEND_PORT}:3000"
    depends_on:
      postgres-test:
        condition: service_healthy

  selenium:
    image: selenium/standalone-chrome:latest
    ports:
      - "${SELENIUM_PORT}:4444"
    shm_size: '2g'
    networks:
      - test-network

  app-test:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "${APP_PORT}:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres-test:5432/${POSTGRES_TEST_DB}
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_TEST_USER}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_TEST_PASSWORD}
      SPRING_PROFILES_ACTIVE: test
      SERVER_PORT: 8080
    depends_on:
      postgres-test:
        condition: service_healthy
      frontend-test:
        condition: service_started
    networks:
      - test-network

networks:
  test-network:
    driver: bridge
"""

    // Запуск инфраструктуры
    sh 'docker-compose -f docker-compose-ui.yml up -d'

    // Ожидание запуска сервисов
    sleep 30

    // Проверка статусов
    sh """
        docker-compose -f docker-compose-ui.yml ps
        curl -f http://localhost:${FRONTEND_PORT} || echo "Frontend запускается..."
        curl -f http://localhost:${APP_PORT}/actuator/health || echo "Backend запускается..."
    """

    // Запуск UI тестов
    try {
        sh """
            mvn test -Dtest="*UiTest*" \
                -Dselenide.remote=http://localhost:${SELENIUM_PORT}/wd/hub \
                -Dselenide.baseUrl=http://localhost:${FRONTEND_PORT} \
                -Dselenide.browser=chrome \
                -Dallure.results.directory=target/ui-allure-results
        """
    } finally {
        // Сохранение скриншотов и логов
        sh """
            docker-compose -f docker-compose-ui.yml logs > target/docker-compose-ui.log 2>&1
            docker exec \$(docker-compose -f docker-compose-ui.yml ps -q selenium) ls -la /home/seluser/Downloads/ || true
        """

        // Копирование скриншотов если есть
        sh '''
            mkdir -p target/screenshots || true
            docker cp $(docker-compose -f docker-compose-ui.yml ps -q selenium):/home/seluser/Downloads/ target/screenshots/ 2>/dev/null || true
        '''
    }
}

def runUiTestsLocally() {
    echo 'Запуск UI тестов локально...'

    // Проверка наличия Chrome
    sh 'which google-chrome-stable || which chromium-browser || echo "Chrome не найден, требуется установка"'

    // Запуск тестов
    sh """
        mvn test -Dtest="*UiTest*" \
            -Dselenide.baseUrl=http://localhost:3000 \
            -Dselenide.browser=chrome \
            -Dallure.results.directory=target/ui-allure-results
    """
}

def runIntegrationTests() {
    echo 'Запуск полных интеграционных тестов...'

    // Используем существующий docker-compose.yml если есть, или создаем
    if (!fileExists('docker-compose.yml')) {
        writeFile file: 'docker-compose.yml', text: """
version: '3.8'
services:
  postgres-test:
    image: postgres:15
    environment:
      POSTGRES_DB: ${POSTGRES_TEST_DB}
      POSTGRES_USER: ${POSTGRES_TEST_USER}
      POSTGRES_PASSWORD: ${POSTGRES_TEST_PASSWORD}
    ports:
      - "${POSTGRES_TEST_PORT}:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_TEST_USER}"]
      interval: 5s
      timeout: 5s
      retries: 5

  frontend-test:
    image: ${DOCKER_IMAGE}:${DOCKER_TAG}
    ports:
      - "${FRONTEND_PORT}:3000"
    depends_on:
      postgres-test:
        condition: service_healthy

  selenium:
    image: selenium/standalone-chrome:latest
    ports:
      - "${SELENIUM_PORT}:4444"
    shm_size: '2g'

  app-test:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "${APP_PORT}:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres-test:5432/${POSTGRES_TEST_DB}
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_TEST_USER}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_TEST_PASSWORD}
      SPRING_PROFILES_ACTIVE: test
      SERVER_PORT: 8080
    depends_on:
      postgres-test:
        condition: service_healthy
      frontend-test:
        condition: service_started
"""
    }

    // Запуск всей инфраструктуры
    sh 'docker-compose up -d'
    sleep 45

    // Запуск всех тестов
    try {
        sh """
            mvn verify \
                -Dselenide.remote=http://localhost:${SELENIUM_PORT}/wd/hub \
                -Dselenide.baseUrl=http://localhost:${FRONTEND_PORT} \
                -Dspring.profiles.active=test \
                -Dserver.port=${APP_PORT} \
                -Dspring.datasource.url=jdbc:postgresql://localhost:${POSTGRES_TEST_PORT}/${POSTGRES_TEST_DB} \
                -Dspring.datasource.username=${POSTGRES_TEST_USER} \
                -Dspring.datasource.password=${POSTGRES_TEST_PASSWORD}
        """
    } finally {
        sh 'docker-compose logs > target/docker-compose-full.log 2>&1'
    }
}

def cleanupDocker() {
    echo 'Очистка Docker контейнеров и образов...'

    // Остановка и удаление контейнеров
    sh '''
        docker-compose down -v 2>/dev/null || true
        docker-compose -f docker-compose-ui.yml down -v 2>/dev/null || true

        docker stop app-test postgres-test selenium local-postgres-test 2>/dev/null || true
        docker rm app-test postgres-test selenium local-postgres-test 2>/dev/null || true

        # Удаление неиспользуемых образов
        docker image prune -af 2>/dev/null || true

        # Удаление сетей
        docker network rm test-network 2>/dev/null || true
    '''
}