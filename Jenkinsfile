pipeline {
    agent any

    tools {
        maven 'Maven-3.6.3'
        jdk 'JDK-17'
    }

    stages {
        // Этап 1: Получение исходного кода
        stage('Получение кода') {
            steps {
                checkout scm
            }
        }

        // Этап 2: Компиляция проекта
        stage('Компиляция проекта') {
            steps {
                sh 'mvn clean compile'
            }
        }

        // Этап 3: Модульные тесты (без UI)
        stage('Модульные тесты') {
            steps {
                sh 'mvn test -Dtest=!*UiTest*'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        // Этап 4: Запуск бэкенда для API тестов
        stage('Запуск бэкенда') {
            steps {
                script {
                    sh '''
                        echo "=== ЗАПУСК БЭКЕНДА ==="

                        # Собираем приложение
                        mvn clean package -DskipTests

                        # Запускаем бэкенд (подключаемся к существующей БД на порту 5434)
                        java -jar target/*.jar \
                            --spring.datasource.url=jdbc:postgresql://localhost:5434/avito \
                            --spring.datasource.username=postgres \
                            --spring.datasource.password=password \
                            --server.port=8080 > backend.log 2>&1 &
                        echo $! > backend.pid

                        # Ждем запуска
                        sleep 30

                        # Проверяем здоровье
                        echo "Проверка бэкенда..."
                        curl --retry 10 --retry-delay 5 --retry-max-time 60 \
                             http://localhost:8080/actuator/health || echo "Бэкенд не запустился"
                    '''
                }
            }
        }

        // Этап 5: API тесты
        stage('API тесты') {
            steps {
                sh 'mvn test -Dtest=*ApiTest*'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                    script {
                        if (fileExists('target/allure-results')) {
                            allure([
                                includeProperties: false,
                                jdk: '',
                                properties: [],
                                reportBuildPolicy: 'ALWAYS',
                                results: [[path: 'target/allure-results']]
                            ])
                        }
                    }
                }
            }
        }

        // Этап 6: UI тесты через Docker Selenium
        stage('UI тесты через Docker Selenium') {
            when {
                expression {
                    return env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'develop'
                }
            }
            steps {
                script {
                    echo "=== ЗАПУСК UI ТЕСТОВ ЧЕРЕЗ DOCKER SELENIUM ==="

                    // 1. Проверяем, что фронтенд запущен
                    sh '''
                        echo "Проверка фронтенда..."
                        if ! docker ps --format "{{.Names}}" | grep -q frontend-avito; then
                            echo "ВНИМАНИЕ: Фронтенд не запущен!"
                            echo "Запускаем фронтенд..."
                            docker run -d --name frontend-avito \
                                -p 3000:3000 \
                                ghcr.io/dmitry-bizin/front-react-avito:v1.21
                            sleep 10
                        fi

                        echo "Проверка доступности фронтенда..."
                        curl -s http://localhost:3000 > /dev/null && echo "Фронтенд доступен" || echo "Фронтенд не отвечает"
                    '''

                    // 2. Запускаем Selenium в Docker
                    sh '''
                        echo "Запуск Selenium Chrome в Docker..."

                        # Останавливаем старый контейнер если есть
                        docker stop selenium-chrome 2>/dev/null || true
                        docker rm selenium-chrome 2>/dev/null || true

                        # Запускаем новый контейнер с Selenium
                        docker run -d \
                            --name selenium-chrome \
                            -p 4444:4444 \
                            -p 7900:7900 \
                            --shm-size="2g" \
                            -e SE_NODE_MAX_SESSIONS=10 \
                            -e SE_NODE_OVERRIDE_MAX_SESSIONS=true \
                            -e SE_SCREEN_WIDTH=1920 \
                            -e SE_SCREEN_HEIGHT=1200 \
                            -e SE_VNC_NO_PASSWORD=1 \
                            --network="host" \
                            selenium/standalone-chrome:latest

                        echo "Ожидание запуска Selenium..."
                        sleep 15

                        # Проверяем статус Selenium
                        echo "Проверка статуса Selenium Grid..."
                        curl -s http://localhost:4444/wd/hub/status | jq .value.ready || \
                        curl -s http://localhost:4444/status | jq .value.ready || \
                        echo "Selenium запущен (не смогли получить JSON статус)"

                        echo "Selenium доступен по:"
                        echo "- WebDriver: http://localhost:4444/wd/hub"
                        echo "- VNC просмотр: http://localhost:7900 (no password)"
                    '''

                    // 3. Запускаем UI тесты через Selenium Grid
                    sh '''
                        echo "=== ЗАПУСК UI ТЕСТОВ ==="

                        # Экспортируем переменные для отладки
                        export SELENIDE_REMOTE="http://localhost:4444/wd/hub"
                        export SELENIDE_BASE_URL="http://localhost:3000"

                        echo "Параметры запуска:"
                        echo "- Remote: $SELENIDE_REMOTE"
                        echo "- Base URL: $SELENIDE_BASE_URL"

                        # Запускаем тесты
                        mvn test -Dtest=*UiTest* \
                            -Dselenide.remote=http://localhost:4444/wd/hub \
                            -Dselenide.browser=chrome \
                            -Dselenide.baseUrl=http://localhost:3000 \
                            -Dselenide.timeout=15000 \
                            -Dselenide.browserSize=1920x1200 \
                            -Dselenide.pageLoadStrategy=normal \
                            -Dselenide.reportsFolder=target/screenshots
                    '''
                }
            }
            post {
                always {
                    // Всегда останавливаем и чистим контейнеры
                    sh '''
                        echo "=== ОЧИСТКА КОНТЕЙНЕРОВ ==="

                        # Останавливаем Selenium
                        docker stop selenium-chrome 2>/dev/null || true
                        docker rm selenium-chrome 2>/dev/null || true

                        # Останавливаем фронтенд который мы запускали
                        docker stop frontend-avito 2>/dev/null || true
                        docker rm frontend-avito 2>/dev/null || true
                    '''

                    junit 'target/surefire-reports/**/*.xml'
                    script {
                        if (fileExists('target/allure-results')) {
                            allure([
                                includeProperties: false,
                                jdk: '',
                                properties: [],
                                reportBuildPolicy: 'ALWAYS',
                                results: [[path: 'target/allure-results']]
                            ])
                        }
                    }
                }
                success {
                    echo "✅ UI тесты выполнены успешно!"
                }
                failure {
                    echo "❌ UI тесты упали"

                    // Сохраняем логи Selenium при падении
                    sh '''
                        echo "=== СОХРАНЕНИЕ ЛОГОВ SELENIUM ==="
                        docker logs selenium-chrome > selenium.log 2>&1 || true
                        echo "Логи Selenium сохранены в selenium.log"
                    '''
                    archiveArtifacts artifacts: 'selenium.log', allowEmptyArchive: true
                }
            }
        }

        // Этап 7: Остановка бэкенда
        stage('Остановка бэкенда') {
            steps {
                script {
                    sh '''
                        echo "Остановка бэкенда..."
                        if [ -f backend.pid ]; then
                            kill $(cat backend.pid) 2>/dev/null || true
                            rm -f backend.pid backend.log
                        fi

                        # Дополнительная проверка
                        pkill -f "java -jar target/.*.jar" || true
                    '''
                }
            }
        }

        // Этап 8: Сборка пакета
        stage('Сборка пакета') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        always {
            // Гарантированная очистка
            script {
                sh '''
                    echo "=== ФИНАЛЬНАЯ ОЧИСТКА ==="

                    # Останавливаем бэкенд если еще не остановлен
                    if [ -f backend.pid ]; then
                        kill $(cat backend.pid) 2>/dev/null || true
                        rm -f backend.pid
                    fi

                    # Останавливаем все связанные контейнеры
                    docker stop selenium-chrome frontend-avito 2>/dev/null || true
                    docker rm selenium-chrome frontend-avito 2>/dev/null || true

                    # Останавливаем Java процессы
                    pkill -f "spring-boot:run" || true
                    pkill -f "java -jar target/.*.jar" || true
                '''
            }
            cleanWs()
        }
        success {
            echo "✅ Сборка #${env.BUILD_NUMBER} УСПЕШНА!"
        }
        failure {
            echo "❌ Сборка #${env.BUILD_NUMBER} ПРОВАЛЕНА"
        }
        unstable {
            echo "⚠️ Сборка #${env.BUILD_NUMBER} НЕУСТОЙЧИВА (сломанные тесты)"
        }
    }
}