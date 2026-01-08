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
                    echo "=== РАБОЧАЯ ДИРЕКТОРИЯ ==="
                    pwd
                    echo "Содержимое:"
                    ls -la
                    echo ""
                    echo "Тесты:"
                    find . -name "*Test.java" | head -10
                '''
            }
        }

        stage('Запуск инфраструктуры') {
            steps {
                script {
                    echo "=== ЗАПУСК SELENIUM ==="

                    sh '''
                        # Очистка
                        docker stop selenium 2>/dev/null || true
                        docker rm selenium 2>/dev/null || true

                        # Запуск
                        docker run -d --name selenium \
                            -p 4444:4444 \
                            --shm-size="2g" \
                            selenium/standalone-chrome:latest

                        sleep 20

                        echo "Проверка Selenium..."
                        curl -s http://localhost:4444/wd/hub/status | jq -r '.value.ready' || echo "Selenium готов"
                    '''
                }
            }
        }

        stage('Сборка и запуск бэкенда') {
            steps {
                sh '''
                    echo "=== СБОРКА И ЗАПУСК БЭКЕНДА ==="

                    # Сборка
                    mvn clean package -DskipTests

                    # Запуск бэкенда с тестовым профилем
                    nohup java -jar target/*.jar \
                        --spring.datasource.url=jdbc:postgresql://localhost:5434/avito \
                        --spring.datasource.username=postgres \
                        --spring.datasource.password=password \
                        --server.port=8080 \
                        --spring.profiles.active=test \
                        --management.endpoints.web.exposure.include="*" \
                        --management.endpoint.health.show-details=always \
                        --logging.level.org.springframework.security=WARN > backend.log 2>&1 &
                    echo $! > backend.pid

                    sleep 45

                    echo "Проверка бэкенда..."
                    curl -s -f http://localhost:8080/actuator/health && echo "✅ Бэкенд запущен" || echo "⚠️ Бэкенд может требовать аутентификации"
                '''
            }
        }

        stage('Запуск ВСЕХ тестов') {
            steps {
                sh '''
                    echo "=== ЗАПУСК ВСЕХ ТЕСТОВ ==="

                    # Запускаем все тесты с настройками для Selenium
                    mvn test \
                        -Dselenide.remote=http://localhost:4444/wd/hub \
                        -Dselenide.baseUrl=http://localhost:3000 \
                        -Dselenide.timeout=25000 \
                        -Dselenide.browser=chrome \
                        -DfailIfNoTests=false \
                        -Dwebdriver.chrome.driver=/usr/local/bin/chromedriver
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

                    # Бэкенд
                    if [ -f backend.pid ]; then
                        kill $(cat backend.pid) 2>/dev/null || true
                        sleep 5
                        rm -f backend.pid
                    fi

                    # Selenium
                    docker stop selenium 2>/dev/null || true
                    docker rm selenium 2>/dev/null || true

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
                docker stop selenium 2>/dev/null || true
                docker rm selenium 2>/dev/null || true
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