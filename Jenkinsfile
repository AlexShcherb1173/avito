pipeline {
    agent any

    tools {
        maven 'Maven-3.6.3'
        jdk 'JDK-17'
    }

    stages {
        stage('Подготовка') {
            steps {
                checkout scm
                sh '''
                    echo "=== ОЧИСТКА СТАРЫХ КОНТЕЙНЕРОВ ==="
                    docker stop selenium-test 2>/dev/null || true
                    docker rm selenium-test 2>/dev/null || true
                '''
            }
        }

        stage('Проверка сервисов') {
            steps {
                sh '''
                    echo "=== ПРОВЕРКА СЕРВИСОВ ==="

                    echo "1. Фронтенд (порт 3000):"
                    curl -s http://localhost:3000 > /dev/null && echo "✅ Доступен" || echo "❌ Не доступен"

                    echo "2. PostgreSQL (порт 5434):"
                    pg_isready -h localhost -p 5434 2>/dev/null && echo "✅ Доступна" || echo "❌ Не доступна"

                    echo "3. Docker:"
                    docker --version && echo "✅ Работает" || echo "❌ Не работает"
                '''
            }
        }

        stage('Запуск Selenium') {
            steps {
                script {
                    echo "=== ЗАПУСК SELENIUM В DOCKER ==="

                    sh '''
                        echo "Останавливаем старый Selenium..."
                        docker stop selenium-test 2>/dev/null || true
                        docker rm selenium-test 2>/dev/null || true

                        echo "Запускаем новый Selenium..."
                        docker run -d --name selenium-test \
                            -p 4444:4444 \
                            --shm-size="2g" \
                            -e SE_NODE_MAX_SESSIONS=10 \
                            selenium/standalone-chrome:4.16.1

                        echo "Ждем запуска (15 секунд)..."
                        sleep 15

                        echo "Проверка Selenium Grid..."
                        curl -s http://localhost:4444/wd/hub/status | jq -r '.value.ready' && echo "✅ Готов" || echo "⚠️ Проверка не прошла, но продолжим"
                    '''
                }
            }
        }

        stage('Запуск бэкенда') {
            steps {
                sh '''
                    echo "=== ЗАПУСК БЭКЕНДА ==="

                    # Останавливаем старый бэкенд
                    pkill -f "java -jar target/.*.jar" 2>/dev/null || true

                    # Собираем приложение
                    mvn clean package -DskipTests

                    # Запускаем бэкенд
                    nohup java -jar target/*.jar \
                        --spring.datasource.url=jdbc:postgresql://localhost:5434/avito \
                        --spring.datasource.username=postgres \
                        --spring.datasource.password=password \
                        --server.port=8080 \
                        --spring.profiles.active=test > backend.log 2>&1 &
                    echo $! > backend.pid

                    echo "Ждем запуска бэкенда (30 секунд)..."
                    sleep 30

                    echo "Проверка бэкенда..."
                    curl -s http://localhost:8080/actuator/health && echo "✅ Бэкенд запущен" || echo "⚠️ Бэкенд не отвечает, но продолжим"
                '''
            }
        }

        stage('Запуск API тестов') {
            steps {
                sh '''
                    echo "=== ЗАПУСК API ТЕСТОВ ==="
                    mvn test -Dtest=*ApiTest* \
                        -Dspring.datasource.url=jdbc:postgresql://localhost:5434/avito \
                        -Dspring.datasource.username=postgres \
                        -Dspring.datasource.password=password
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Запуск UI тестов') {
            steps {
                sh '''
                    echo "=== ЗАПУСК UI ТЕСТОВ ==="

                    # Запускаем UI тесты через Selenium Grid
                    mvn test -Dtest=*UiTest* \
                        -Dselenide.remote=http://localhost:4444/wd/hub \
                        -Dselenide.browser=chrome \
                        -Dselenide.baseUrl=http://localhost:3000 \
                        -Dselenide.timeout=20000 \
                        -Dselenide.browserSize=1920x1200 \
                        -Dselenide.headless=false
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                    archiveArtifacts artifacts: 'backend.log', allowEmptyArchive: true
                }
            }
        }

        stage('Очистка') {
            steps {
                sh '''
                    echo "=== ОЧИСТКА ==="

                    # Останавливаем бэкенд
                    if [ -f backend.pid ]; then
                        kill $(cat backend.pid) 2>/dev/null || true
                        rm -f backend.pid backend.log
                    fi

                    # Останавливаем Selenium
                    docker stop selenium-test 2>/dev/null || true
                    docker rm selenium-test 2>/dev/null || true

                    # Дополнительная очистка
                    pkill -f "java -jar target/.*.jar" 2>/dev/null || true
                '''
            }
        }
    }

    post {
        always {
            // Гарантированная очистка
            sh '''
                docker stop selenium-test 2>/dev/null || true
                docker rm selenium-test 2>/dev/null || true
                pkill -f "java -jar target/.*.jar" 2>/dev/null || true
            '''
            cleanWs()
        }
        success {
            echo "✅ Сборка #${env.BUILD_NUMBER} УСПЕШНА!"
        }
        failure {
            echo "❌ Сборка #${env.BUILD_NUMBER} ПРОВАЛЕНА"
        }
    }
}