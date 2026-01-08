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

                        # Запускаем бэкенд
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
                    echo "=== ДИАГНОСТИКА И ЗАПУСК UI ТЕСТОВ ==="

                    // 1. Проверяем сеть и порты
                    sh '''
                        echo "=== ПРОВЕРКА СЕТИ ==="
                        echo "IP адреса:"
                        ip addr show | grep inet

                        echo "Открытые порты:"
                        netstat -tlnp | grep :4444 || echo "Порт 4444 не слушается"
                        netstat -tlnp | grep :3000 || echo "Порт 3000 не слушается"
                        netstat -tlnp | grep :8080 || echo "Порт 8080 не слушается"

                        echo "Проверка доступности фронтенда..."
                        timeout 10 curl -f http://localhost:3000 && echo "Фронтенд доступен" || echo "Фронтенд недоступен"
                    '''

                    // 2. Запускаем Selenium с правильными настройками
                    sh '''
                        echo "=== ЗАПУСК SELENIUM ==="

                        # Удаляем старый контейнер
                        docker rm -f selenium-chrome 2>/dev/null || true

                        # Запускаем Selenium 4 с Chrome
                        docker run -d \
                            --name selenium-chrome \
                            -p 4444:4444 \
                            -p 7900:7900 \
                            --shm-size="2g" \
                            -e SE_EVENT_BUS_HOST=localhost \
                            -e SE_EVENT_BUS_PUBLISH_PORT=4442 \
                            -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 \
                            -e SE_NODE_SESSION_TIMEOUT=300 \
                            -e SE_NODE_MAX_SESSIONS=5 \
                            -e SE_NODE_OVERRIDE_MAX_SESSIONS=true \
                            -e JAVA_OPTS="-Xmx512m" \
                            --add-host host.docker.internal:host-gateway \
                            selenium/standalone-chrome:4.16.1-20231219

                        echo "Ждем запуска Selenium (30 секунд)..."
                        sleep 30

                        # Проверяем запуск несколькими способами
                        echo "Проверка Selenium Grid..."

                        # Способ 1: Проверка статуса через API v4
                        echo "API v4 status:"
                        curl -s http://localhost:4444/status | jq -r '.value.ready' || echo "Не удалось получить статус"

                        # Способ 2: Проверка через wd/hub
                        echo "WD Hub status:"
                        curl -s http://localhost:4444/wd/hub/status | jq -r '.value.ready' || echo "Не удалось получить статус"

                        # Способ 3: Простая проверка
                        echo "Простая проверка доступности..."
                        curl -f http://localhost:4444 && echo "Selenium отвечает" || echo "Selenium не отвечает"

                        # Проверяем логи контейнера
                        echo "Последние 10 строк логов Selenium:"
                        docker logs selenium-chrome --tail 10
                    '''

                    // 3. Запускаем тесты с отладкой
                    sh '''
                        echo "=== ЗАПУСК ТЕСТОВ С ОТЛАДКОЙ ==="

                        # Создаем конфигурационный файл для Selenide
                        cat > selenide.properties << EOF
                        browser=chrome
                        remote=http://localhost:4444/wd/hub
                        baseUrl=http://localhost:3000
                        timeout=20000
                        browserSize=1920x1200
                        pageLoadStrategy=normal
                        headless=false
                        proxyEnabled=false
                        browserCapabilities.chrome.args=--no-sandbox,--disable-dev-shm-usage,--disable-gpu,--window-size=1920,1200,--remote-allow-origins=*
                        browserCapabilities.chrome.prefs.intl.accept_languages=ru
                        browserCapabilities.acceptInsecureCerts=true
                        browserCapabilities.se:options.enableVNC=true
                        EOF

                        echo "Конфигурация Selenide:"
                        cat selenide.properties

                        # Запускаем тесты с максимальной отладкой
                        mvn test -Dtest=*UiTest* \
                            -Dselenide.remote=http://localhost:4444/wd/hub \
                            -Dselenide.browser=chrome \
                            -Dselenide.baseUrl=http://localhost:3000 \
                            -Dselenide.timeout=30000 \
                            -Dselenide.browserSize=1920x1200 \
                            -Dselenide.headless=false \
                            -Dselenide.pageLoadStrategy=normal \
                            -Dselenide.reportsFolder=target/screenshots \
                            -Dselenide.downloadsFolder=target/downloads \
                            -e -X
                    '''

                    // 4. Сохраняем логи при ошибке
                    sh '''
                        echo "=== СОХРАНЕНИЕ ЛОГОВ ДЛЯ ДИАГНОСТИКИ ==="
                        docker logs selenium-chrome > selenium_full.log 2>&1
                        echo "Логи Selenium сохранены в selenium_full.log"

                        # Сохраняем состояние Docker
                        docker ps -a > docker_state.log
                        docker images > docker_images.log
                    '''
                }
            }
            post {
                always {
                    sh '''
                        echo "=== ОЧИСТКА ==="
                        docker rm -f selenium-chrome 2>/dev/null || true
                    '''
                    junit 'target/surefire-reports/**/*.xml'
                    archiveArtifacts artifacts: 'selenium_full.log,docker_state.log,docker_images.log', allowEmptyArchive: true
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
                    docker stop selenium-chrome 2>/dev/null || true
                    docker rm selenium-chrome 2>/dev/null || true

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