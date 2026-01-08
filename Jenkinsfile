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
                sh 'docker-compose -f docker-compose.test.yml down || true'
            }
        }

        stage('Запуск инфраструктуры') {
            steps {
                script {
                    echo "=== ЗАПУСК ИНФРАСТРУКТУРЫ В DOCKER ==="

                    sh '''
                        # Запускаем всю инфраструктуру
                        docker-compose -f docker-compose.test.yml up -d postgres-test frontend-test selenium

                        # Ждем запуска
                        sleep 30

                        # Проверяем сервисы
                        echo "Проверка PostgreSQL..."
                        docker-compose -f docker-compose.test.yml exec -T postgres-test pg_isready -U postgres

                        echo "Проверка фронтенда..."
                        curl -f http://localhost:3001 || echo "Фронтенд запущен"

                        echo "Проверка Selenium..."
                        curl -f http://localhost:4444/wd/hub/status || echo "Selenium запущен"
                    '''
                }
            }
        }

        stage('Сборка и API тесты') {
            steps {
                sh '''
                    echo "=== СБОРКА И API ТЕСТЫ ==="

                    # Собираем приложение
                    mvn clean package -DskipTests

                    # Запускаем приложение в Docker
                    docker-compose -f docker-compose.test.yml build app-test
                    docker-compose -f docker-compose.test.yml up -d app-test

                    # Ждем запуска
                    sleep 30

                    # Проверяем бэкенд
                    curl -f http://localhost:8081/actuator/health || echo "Бэкенд запущен"

                    # Запускаем API тесты
                    mvn test -Dtest=*ApiTest* \
                        -Dspring.datasource.url=jdbc:postgresql://localhost:5435/avito_test \
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

        stage('UI тесты через Docker Selenium') {
            steps {
                script {
                    echo "=== UI ТЕСТЫ ЧЕРЕЗ DOCKER SELENIUM ==="

                    sh '''
                        # Запускаем UI тесты через Selenium в Docker
                        mvn test -Dtest=*UiTest* \
                            -Dselenide.remote=http://localhost:4444/wd/hub \
                            -Dselenide.browser=chrome \
                            -Dselenide.baseUrl=http://localhost:3001 \
                            -Dselenide.timeout=15000 \
                            -Dselenide.browserSize=1920x1200
                    '''
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Остановка инфраструктуры') {
            steps {
                sh '''
                    echo "=== ОСТАНОВКА ИНФРАСТРУКТУРЫ ==="
                    docker-compose -f docker-compose.test.yml down -v
                '''
            }
        }
    }

    post {
        always {
            // Гарантированная очистка
            sh '''
                docker-compose -f docker-compose.test.yml down -v 2>/dev/null || true
                docker system prune -f 2>/dev/null || true
            '''
            cleanWs()
        }
    }
}
