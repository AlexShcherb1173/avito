pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'JDK 11'
    }

    stages {
        stage('Очистка и компиляция') {
            steps {
                cleanWs()
                sh 'mvn clean compile'
            }
        }

        stage('API тесты') {
            steps {
                sh 'mvn test -Dtest="*ApiTest"'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('UI тесты') {
            steps {
                // Предполагаем, что приложение уже запущено на localhost:3000
                sh '''
                    mvn test -Dtest="*UiTest" \
                    -Dselenide.browser=chrome \
                    -Dselenide.headless=true
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
    }

    post {
        always {
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