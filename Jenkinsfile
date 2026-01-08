pipeline {
    agent any

    tools {
        maven 'Maven-3.6.3'
        jdk 'JDK-17'
    }

    stages {
        stage('Все тесты') {
            steps {
                checkout scm

                sh '''
                    # Установка Xvfb
                    apt-get update && apt-get install -y xvfb

                    # Запуск Xvfb
                    Xvfb :99 -screen 0 1920x1200x24 &
                    export DISPLAY=:99
                    sleep 2

                    # Запуск ВСЕХ тестов
                    mvn clean test
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
    }
}