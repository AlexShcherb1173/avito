pipeline {
    agent any

    tools {
        // Используйте имена, которые есть в вашем Jenkins
        // Посмотрите в Jenkins: Manage Jenkins -> Tools
        maven 'Maven-3.6.3'  // Имя из вашего Jenkins
        jdk 'JDK-17'         // Имя из вашего Jenkins
    }

    parameters {
        choice(
            name: 'TEST_TYPE',
            choices: ['all', 'api', 'ui'],
            description: 'Выберите тип тестов для запуска'
        )
        booleanParam(
            name: 'RUN_WITH_DOCKER',
            defaultValue: true,
            description: 'Запускать UI тесты через Docker Selenium?'
        )
    }

    environment {
        DOCKER_HOST = 'unix:///var/run/docker.sock'
        APPLICATION_PORT = '3000'
        SELENIUM_URL = 'http://localhost:4444/wd/hub'
    }

    stages {
        stage('Очистка рабочей области') {
            steps {
                cleanWs()
            }
        }

        stage('Подготовка окружения') {
            steps {
                script {
                    echo "Тип тестов: ${params.TEST_TYPE}"
                    echo "Запуск с Docker: ${params.RUN_WITH_DOCKER}"
                    echo "Java версия: ${JAVA_HOME}"
                    echo "Maven версия: ${tool 'Maven-3.6.3'}"
                }
            }
        }

        stage('Проверка кода') {
            steps {
                sh '''
                    echo "Java version:"
                    java -version
                    echo "Maven version:"
                    mvn --version
                '''
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Запуск API тестов') {
            when {
                anyOf {
                    expression { params.TEST_TYPE == 'all' }
                    expression { params.TEST_TYPE == 'api' }
                }
            }
            steps {
                sh '''
                    mvn test -Dtest="*ApiTest,*ApiTest*" \
                    -DfailIfNoTests=false
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Запуск UI тестов') {
            when {
                anyOf {
                    expression { params.TEST_TYPE == 'all' }
                    expression { params.TEST_TYPE == 'ui' }
                }
            }
            stages {
                stage('Запуск Selenium Grid в Docker') {
                    when {
                        expression { params.RUN_WITH_DOCKER == true }
                    }
                    steps {
                        script {
                            // Проверяем доступность Docker
                            sh 'docker --version'

                            // Запускаем Selenium Grid
                            sh '''
                                docker run -d --name selenium-hub \
                                    -p 4442:4442 -p 4443:4443 -p 4444:4444 \
                                    selenium/hub:4.11.0 2>/dev/null || true

                                sleep 10

                                docker run -d --name selenium-chrome \
                                    --shm-size="2g" \
                                    -e SE_EVENT_BUS_HOST=localhost \
                                    -e SE_EVENT_BUS_PUBLISH_PORT=4442 \
                                    -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 \
                                    -v /dev/shm:/dev/shm \
                                    selenium/node-chrome:4.11.0 2>/dev/null || true

                                sleep 15
                            '''
                        }
                    }
                }

                stage('Запуск приложения') {
                    steps {
                        script {
                            // Проверяем, запущено ли уже приложение
                            def isAppRunning = sh(
                                script: 'curl -s -o /dev/null -w "%{http_code}" http://localhost:3000 || echo "404"',
                                returnStdout: true
                            ).trim()

                            if (isAppRunning != "200") {
                                echo "Запускаем приложение..."
                                sh '''
                                    # Запуск приложения
                                    nohup mvn spring-boot:run \
                                        -Dspring-boot.run.arguments="--server.port=3000" \
                                        > app.log 2>&1 &

                                    # Ждем старта приложения (максимум 60 секунд)
                                    COUNTER=0
                                    while [ $COUNTER -lt 30 ]; do
                                        sleep 2
                                        HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:3000 || echo "000")
                                        if [ "$HTTP_CODE" = "200" ]; then
                                            echo "Приложение запущено на порту 3000"
                                            break
                                        fi
                                        COUNTER=$((COUNTER + 1))
                                        echo "Ожидание запуска приложения... ($COUNTER/30)"
                                    done

                                    if [ "$HTTP_CODE" != "200" ]; then
                                        echo "Ошибка: приложение не запустилось"
                                        exit 1
                                    fi
                                '''
                            } else {
                                echo "Приложение уже запущено"
                            }
                        }
                    }
                }

                stage('Выполнение UI тестов') {
                    steps {
                        script {
                            if (params.RUN_WITH_DOCKER == true) {
                                sh '''
                                    echo "Запуск UI тестов с Selenium в Docker..."
                                    mvn test -Dtest="*UiTest,*UiTest*" \
                                    -Dselenide.remote=http://localhost:4444/wd/hub \
                                    -Dselenide.browser=chrome \
                                    -DfailIfNoTests=false
                                '''
                            } else {
                                sh '''
                                    echo "Запуск UI тестов локально..."
                                    mvn test -Dtest="*UiTest,*UiTest*" \
                                    -Dselenide.browser=chrome \
                                    -Dselenide.headless=true \
                                    -DfailIfNoTests=false
                                '''
                            }
                        }
                    }
                    post {
                        always {
                            junit 'target/surefire-reports/**/*.xml'
                        }
                    }
                }
            }
        }

        stage('Сборка отчета Allure') {
            steps {
                script {
                    // Проверяем наличие результатов Allure
                    def hasAllureResults = fileExists('target/allure-results')
                    if (hasAllureResults) {
                        allure([
                            includeProperties: false,
                            jdk: '',
                            properties: [],
                            reportBuildPolicy: 'ALWAYS',
                            results: [[path: 'target/allure-results']]
                        ])
                    } else {
                        echo 'Результаты Allure не найдены'
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                // Останавливаем контейнеры Docker
                if (params.RUN_WITH_DOCKER == true) {
                    sh '''
                        docker stop selenium-chrome selenium-hub 2>/dev/null || true
                        docker rm selenium-chrome selenium-hub 2>/dev/null || true
                    '''
                }

                // Останавливаем приложение
                sh '''
                    pkill -f "spring-boot:run" 2>/dev/null || true
                    pkill -f "java.*app.jar" 2>/dev/null || true
                '''

                // Архивируем результаты
                archiveArtifacts artifacts: 'target/surefire-reports/**/*.*', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                archiveArtifacts artifacts: 'app.log', allowEmptyArchive: true
            }
        }
        success {
            echo 'Все тесты выполнены успешно! ✅'
        }
        failure {
            echo 'В ходе выполнения пайплайна возникли ошибки ❌'
        }
        unstable {
            echo 'Некоторые тесты не прошли'
        }
    }
}