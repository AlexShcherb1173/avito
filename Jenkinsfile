// Jenkinsfile - Конвейер непрерывной интеграции и поставки (CI/CD)
// Этот файл определяет все этапы автоматической сборки, тестирования и развертывания

// Основной блок пайплайна Jenkins
pipeline {
    // Указываем, что задачи могут выполняться на любом доступном агенте (ноде)
    agent any

    // Настройка инструментов, которые будут использоваться в пайплайне
    tools {
        // Указываем, какая версия Maven будет использоваться
        // 'Maven-3.8.6' - это имя предварительно настроенного инструмента в Jenkins
        maven 'Maven-3.6.3'

        // Указываем, какая версия JDK будет использоваться
        // 'JDK-11' - это имя предварительно настроенного инструмента в Jenkins
        jdk 'JDK-17'
    }

    // Блок для определения переменных окружения
    environment {
        // Переменная для указания браузера для UI тестов
        BROWSER = 'chrome'

        // URL адрес пользовательского интерфейса для UI тестов
        UI_URL = 'http://localhost:3000'

        // Дополнительные переменные можно добавить здесь по мере необходимости
        // Например: DATABASE_URL = 'jdbc:postgresql://localhost:5432/mydb'
    }

    // Блок stages определяет последовательность этапов пайплайна
    stages {
        // Этап 1: Получение исходного кода из системы контроля версий
        stage('Получение кода') {
            steps {
                // Команда checkout загружает код из репозитория Git
                checkout scm  // scm - Source Control Management (система управления версиями)
            }
        }

        // Этап 2: Компиляция проекта
        stage('Компиляция проекта') {
            steps {
                // Выполняем Maven команду для очистки и компиляции проекта
                // 'mvn clean compile' удаляет предыдущие сборки и компилирует исходный код
                sh 'mvn clean compile'
            }
        }

        // Этап 3: Запуск модульных тестов
        stage('Модульные тесты') {
            steps {
                // Запускаем Maven тесты, исключая UI и API тесты
                // -Dtest=!*UiTest* - исключить все классы с UiTest в имени
                // -Dtest=!*ApiTest* - исключить все классы с ApiTest в имени
                sh 'mvn test -Dtest=!*UiTest* -Dtest=!*ApiTest*'
            }
            // Блок post выполняется после этапа независимо от результата
            post {
                always {
                    // Сохраняем отчеты JUnit для отображения в Jenkins
                    // **/*.xml - рекурсивный поиск всех XML файлов
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        // Этап 4: Запуск приложения для интеграционных тестов
        stage('Запуск приложения для тестов') {
            steps {
                script {
                    // Запускаем Spring Boot приложение в фоновом режиме
                    // & в конце команды запускает процесс в фоне
                    // -Dspring-boot.run.profiles=test использует профиль 'test' из application-test.properties
                    sh 'mvn spring-boot:run -Dspring-boot.run.profiles=test &'

                    // Ждем 30 секунд, чтобы приложение успело запуститься
                    sleep time: 30, unit: 'SECONDS'

                    // Проверяем, что приложение успешно запустилось
                    // curl делает HTTP запрос к эндпоинту здоровья приложения
                    // --retry 10 - попытаться 10 раз при неудаче
                    // --retry-delay 5 - ждать 5 секунд между попытками
                    // --retry-max-time 60 - общее максимальное время 60 секунд
                    sh '''
                        curl --retry 10 --retry-delay 5 --retry-max-time 60 \
                             http://localhost:8080/actuator/health || exit 1
                    '''
                }
            }
        }

        // Этап 5: Запуск API тестов
        stage('API тесты') {
            steps {
                // Запускаем только API тесты
                // *ApiTest* - запустить все классы с ApiTest в имени
                sh 'mvn test -Dtest=*ApiTest*'
            }
            post {
                always {
                    // Сохраняем отчеты JUnit
                    junit 'target/surefire-reports/**/*.xml'

                    // Генерируем Allure отчеты для красивой визуализации тестов
                    allure([
                        includeProperties: false,  // Не включать свойства в отчет
                        jdk: '',                   // Версия JDK (оставить пустым для автоматического определения)
                        properties: [],            // Дополнительные свойства
                        reportBuildPolicy: 'ALWAYS',  // Всегда генерировать отчет
                        results: [[path: 'target/allure-results']]  // Путь к результатам тестов
                    ])
                }
            }
        }

          // Этап 6: Запуск UI тестов (только для основных веток)
               stage('UI тесты') {
                   // Условие when определяет, когда этот этап должен выполняться
                   when {
                       expression {
                           // Выполнять UI тесты только для веток 'main' или 'develop'
                           // env.BRANCH_NAME - переменная Jenkins с именем текущей ветки
                           return env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'develop'
                       }
                   }
                   steps {
                       script {
                           // Установка Chrome и ChromeDriver для UI тестов
                           sh '''
                               # Обновление списка пакетов
                               apt-get update

                               # Установка необходимых утилит
                               apt-get install -y wget unzip

                               # Добавление ключа Google Chrome в систему
                               wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -

                               # Добавление репозитория Chrome в sources.list
                               echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list

                               # Обновление пакетов и установка Chrome
                               apt-get update
                               apt-get install -y google-chrome-stable

                               # Проверка версии Chrome
                               google-chrome --version

                               # Установка ChromeDriver (драйвер для управления Chrome)
                               # Получаем мажорную версию Chrome
                               CHROME_VERSION=$(google-chrome --version | awk '{print $3}' | cut -d. -f1)
                               echo "Установка ChromeDriver для Chrome версии: $CHROME_VERSION"

                               # Скачиваем ChromeDriver для этой версии
                               # Используем официальное зеркало
                               wget -q -N https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION
                               DRIVER_VERSION=$(cat LATEST_RELEASE_$CHROME_VERSION)
                               echo "Версия ChromeDriver: $DRIVER_VERSION"

                               wget -q -N https://chromedriver.storage.googleapis.com/$DRIVER_VERSION/chromedriver_linux64.zip

                               # Распаковываем архив
                               unzip -o chromedriver_linux64.zip

                               # Даем права на выполнение и перемещаем в системную директорию
                               chmod +x chromedriver
                               mv chromedriver /usr/local/bin/

                               # Проверяем установку
                               echo "Проверка ChromeDriver:"
                               chromedriver --version

                               # Также устанавливаем переменную окружения для WebDriver
                               export WEBDRIVER_CHROME_DRIVER=/usr/local/bin/chromedriver
                               echo "WEBDRIVER_CHROME_DRIVER=$WEBDRIVER_CHROME_DRIVER" >> ~/.bashrc

                               # Альтернативный способ: через WebDriverManager (если используется в проекте)
                               # Устанавливаем WebDriverManager как зависимость если нужно
                           '''

                           // Запускаем UI тесты
                           // *UiTest* - запустить все классы с UiTest в имени
                           // Добавляем системную переменную для ChromeDriver
                           sh '''
                               export WEBDRIVER_CHROME_DRIVER=/usr/local/bin/chromedriver
                               export PATH=/usr/local/bin:$PATH
                               mvn test -Dtest=*UiTest* -Dwebdriver.chrome.driver=/usr/local/bin/chromedriver
                           '''
                       }
                   }
                   post {
                       always {
                           // Сохраняем отчеты JUnit
                           junit 'target/surefire-reports/**/*.xml'

                           // Генерируем Allure отчеты
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

        // Этап 7: Остановка приложения после тестов
        stage('Остановка приложения') {
            steps {
                script {
                    // Останавливаем Spring Boot приложение
                    // pkill -f ищет процесс по строке "spring-boot:run"
                    // || true - если процесс не найден, не считать это ошибкой
                    sh 'pkill -f "spring-boot:run" || true'
                }
            }
        }

        // Этап 8: Сборка финального JAR файла
        stage('Сборка пакета') {
            steps {
                // Собираем JAR файл без запуска тестов
                // -DskipTests пропускает выполнение тестов
                sh 'mvn package -DskipTests'
            }
        }
    }

    // Блок post выполняется после всех этапов
    post {
        // Блок always выполняется всегда, независимо от результата сборки
        always {
            script {
                // Гарантированно останавливаем приложение на случай, если предыдущий этап не сработал
                sh 'pkill -f "spring-boot:run" || true'
            }

            // Очистка рабочего пространства Jenkins
            cleanWs()
        }

        // Блок success выполняется только при успешной сборке
        success {
            // Отправка email уведомления об успешной сборке
            emailext(
                subject: "Сборка успешна: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    Уважаемая команда,

                    Сборка проекта ${env.JOB_NAME} #${env.BUILD_NUMBER} завершена успешно!

                    Детали сборки:
                    - Ветка: ${env.BRANCH_NAME}
                    - Время сборки: ${currentBuild.durationString}
                    - Ссылка на сборку: ${env.BUILD_URL}

                    Все тесты пройдены успешно.

                    С уважением,
                    Jenkins CI/CD
                """,
                to: 'team@example.com'  // Замените на реальный email адрес
            )
        }

        // Блок failure выполняется при неудачной сборке
        failure {
            // Отправка email уведомления о неудачной сборке
            emailext(
                subject: "Сборка провалена: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    Уважаемая команда,

                    Сборка проекта ${env.JOB_NAME} #${env.BUILD_NUMBER} завершена с ошибкой!

                    Детали сборки:
                    - Ветка: ${env.BRANCH_NAME}
                    - Время сборки: ${currentBuild.durationString}
                    - Ссылка на сборку: ${env.BUILD_URL}

                    Пожалуйста, проверьте логи сборки для выявления проблемы.

                    С уважением,
                    Jenkins CI/CD
                """,
                to: 'team@example.com'  // Замените на реальный email адрес
            )
        }

        // Блок unstable выполняется при неустойчивой сборке (например, сломанные тесты)
        unstable {
            // Отправка email уведомления о неустойчивой сборке
            emailext(
                subject: "Сборка неустойчива: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    Уважаемая команда,

                    Сборка проекта ${env.JOB_NAME} #${env.BUILD_NUMBER} завершена неустойчиво!

                    Детали сборки:
                    - Ветка: ${env.BRANCH_NAME}
                    - Время сборки: ${currentBuild.durationString}
                    - Ссылка на сборку: ${env.BUILD_URL}

                    Вероятно, некоторые тесты не пройдены. Проверьте отчеты о тестировании.

                    С уважением,
                    Jenkins CI/CD
                """,
                to: 'rzavsky.ev@gmail.com'  // Замените на реальный email адрес
            )
        }
    }
}