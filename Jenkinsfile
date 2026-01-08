pipeline {
    agent any

    tools {
        maven 'Maven-3.6.3'
        jdk 'JDK-17'
    }

    stages {
        stage('Клонирование и проверка') {
            steps {
                sh '''
                    echo "Java version:"
                    java -version
                    echo ""
                    echo "Maven version:"
                    mvn --version
                    echo ""
                    echo "Current directory:"
                    pwd
                    echo ""
                    echo "Files in directory:"
                    ls -la
                '''
            }
        }

        stage('Сборка') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Тестирование') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Упаковка') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        always {
            archiveArtifacts 'target/*.jar'
        }
    }
}