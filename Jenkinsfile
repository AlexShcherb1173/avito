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

        // Этап 3: Запуск модульных тестов (без UI)
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

        // Этап 4: Запуск приложения для API тестов
        stage('Запуск приложения для тестов') {
            steps {
                script {
                    sh '''
                        # Запускаем Spring Boot приложение в фоновом режиме
                        mvn spring-boot:run -Dspring-boot.run.profiles=test > app.log 2>&1 &
                        echo $! > app.pid

                        # Ждем запуска
                        sleep 30

                        # Проверяем здоровье приложения
                        curl --retry 10 --retry-delay 5 --retry-max-time 60 \
                             http://localhost:8080/actuator/health || echo "Приложение не запустилось"
                    '''
                }
            }
        }

        // Этап 5: Запуск API тестов
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

        // Этап 6: UI тесты (с headless Chrome)
        stage('UI тесты') {
            when {
                expression {
                    return env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'develop'
                }
            }
            steps {
                script {
                    echo "=== НАСТРОЙКА ДЛЯ UI ТЕСТОВ ==="

                    // Устанавливаем необходимые пакеты
                    sh '''
                        apt-get update
                        apt-get install -y wget unzip xvfb

                        # Проверяем Chrome
                        if [ -f /usr/bin/google-chrome ]; then
                            echo "Chrome найден: /usr/bin/google-chrome"
                        else
                            echo "Устанавливаем Chrome..."
                            wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
                            dpkg -i google-chrome-stable_current_amd64.deb || apt-get install -f -y
                        fi

                        # Устанавливаем ChromeDriver
                        CHROME_VERSION=$(google-chrome --version | awk '{print $3}' | cut -d. -f1)
                        echo "Chrome версия: $CHROME_VERSION"

                        wget -q https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION
                        DRIVER_VERSION=$(cat LATEST_RELEASE_$CHROME_VERSION)
                        wget -q https://chromedriver.storage.googleapis.com/$DRIVER_VERSION/chromedriver_linux64.zip
                        unzip -o chromedriver_linux64.zip
                        chmod +x chromedriver
                        mv chromedriver /usr/local/bin/
                    '''

                    // Запускаем UI тесты с Xvfb
                    sh '''
                        # Экспортируем настройки для Selenide
                        export SELENIDE_BROWSER="chrome"
                        export SELENIDE_HEADLESS="true"
                        export SELENIDE_BROWSER_SIZE="1920x1200"
                        export SELENIDE_BASE_URL="http://localhost:3000"

                        # Запускаем тесты в виртуальном framebuffer
                        xvfb-run --server-args="-screen 0 1920x1200x24" \
                            mvn test -Dtest=*UiTest* \
                            -Dselenide.headless=true \
                            -Dselenide.browser="chrome" \
                            -Dselenide.browserSize="1920x1200"
                    '''
                }
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

        // Этап 7: Остановка приложения
        stage('Остановка приложения') {
            steps {
                script {
                    sh '''
                        # Останавливаем приложение если оно было запущено
                        if [ -f app.pid ]; then
                            kill $(cat app.pid) 2>/dev/null || true
                            rm -f app.pid app.log
                        fi
                        # Дополнительная проверка
                        pkill -f "spring-boot:run" || true
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
            script {
                // Гарантированная остановка приложения
                sh 'pkill -f "spring-boot:run" || true'
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
            echo "⚠️ Сборка #${env.BUILD_NUMBER} НЕУСТОЙЧИВА (сломаные тесты)"
        }
    }
}