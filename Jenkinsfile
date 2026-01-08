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
            }
        }

        stage('Запуск Selenium') {
            steps {
                script {
                    echo "=== ЗАПУСК SELENIUM С HOST NETWORK ==="

                    sh '''
                        # Очистка
                        docker stop selenium 2>/dev/null || true
                        docker rm selenium 2>/dev/null || true

                        # Запуск в host network (самый простой способ)
                        docker run -d --name selenium \
                            --network="host" \
                            --shm-size="2g" \
                            selenium/standalone-chrome:latest

                        sleep 25

                        echo "Проверка Selenium..."
                        curl -s http://localhost:4444/wd/hub/status | jq -r '.value.ready' && echo "✅ Selenium готов" || echo "⚠️ Selenium запущен"
                    '''
                }
            }
        }

        stage('Запуск бэкенда') {
            steps {
                sh '''
                    echo "=== ЗАПУСК БЭКЕНДА ==="

                    # Сборка
                    mvn clean package -DskipTests

                    # Запуск
                    nohup java -jar target/*.jar \
                        --spring.datasource.url=jdbc:postgresql://localhost:5434/avito \
                        --spring.datasource.username=postgres \
                        --spring.datasource.password=password \
                        --server.port=8080 \
                        --spring.profiles.active=test > backend.log 2>&1 &
                    echo $! > backend.pid

                    sleep 50

                    echo "Проверка сервисов:"
                    echo "Фронтенд (3000): $(curl -s -o /dev/null -w "%{http_code}" http://localhost:3000 || echo 'off')"
                    echo "Бэкенд (8080): $(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health 2>/dev/null || echo 'requires auth')"
                    echo "Selenium (4444): $(curl -s -o /dev/null -w "%{http_code}" http://localhost:4444 || echo 'off')"
                '''
            }
        }

        stage('Запуск UI тестов') {
            steps {
                sh '''
                    echo "=== ЗАПУСК UI ТЕСТОВ ==="

                    # ТОЛЬКО UI тесты
                    mvn test -Dtest="ru.skypro.homework.aqa.ui.*Test" \
                        -Dselenide.remote=http://localhost:4444/wd/hub \
                        -Dselenide.baseUrl=http://localhost:3000 \
                        -Dselenide.timeout=30000 \
                        -Dselenide.browser=chrome \
                        -DfailIfNoTests=false
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

                    if [ -f backend.pid ]; then
                        kill $(cat backend.pid) 2>/dev/null || true
                        rm -f backend.pid
                    fi

                    docker stop selenium 2>/dev/null || true
                    docker rm selenium 2>/dev/null || true
                '''
            }
        }
    }

    post {
        always {
            sh '''
                docker stop selenium 2>/dev/null || true
                docker rm selenium 2>/dev/null || true
                pkill -f "java -jar target/.*.jar" 2>/dev/null || true
            '''
            cleanWs()
        }
    }
}