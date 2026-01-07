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
                          when {
                              expression {
                                  return env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'develop'
                              }
                          }
                          steps {
                              script {
                                  echo "=== НАСТРОЙКА UI ТЕСТОВ ==="

                                  // 1. Устанавливаем ChromeDriver
                                  sh '''
                                      echo "1. Проверяем Chrome..."
                                      CHROME_PATH="/usr/bin/google-chrome"
                                      echo "Chrome путь: $CHROME_PATH"

                                      # Получаем версию Chrome
                                      CHROME_VERSION=$("$CHROME_PATH" --version 2>&1 | awk '{print $3}' | cut -d. -f1)
                                      echo "Версия Chrome: $CHROME_VERSION"

                                      echo "2. Устанавливаем ChromeDriver..."
                                      # Скачиваем последнюю версию ChromeDriver для этой версии Chrome
                                      wget -q https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION
                                      DRIVER_VERSION=$(cat LATEST_RELEASE_$CHROME_VERSION)
                                      echo "Версия ChromeDriver: $DRIVER_VERSION"

                                      # Скачиваем и распаковываем ChromeDriver
                                      wget -q https://chromedriver.storage.googleapis.com/$DRIVER_VERSION/chromedriver_linux64.zip
                                      unzip -o chromedriver_linux64.zip
                                      chmod +x chromedriver

                                      # Перемещаем в системную директорию
                                      sudo mv chromedriver /usr/local/bin/chromedriver || mv chromedriver /tmp/chromedriver

                                      echo "3. Проверяем установку..."
                                      chromedriver --version || /tmp/chromedriver --version

                                      echo "4. Устанавливаем Xvfb для headless режима..."
                                      sudo apt-get update
                                      sudo apt-get install -y xvfb
                                  '''

                                  // 2. Запускаем UI тесты с Xvfb
                                  sh '''
                                      echo "=== ЗАПУСК UI ТЕСТОВ ==="

                                      # Определяем путь к chromedriver
                                      if [ -f /usr/local/bin/chromedriver ]; then
                                          DRIVER_PATH="/usr/local/bin/chromedriver"
                                      else
                                          DRIVER_PATH="/tmp/chromedriver"
                                      fi

                                      echo "ChromeDriver путь: $DRIVER_PATH"
                                      echo "Chrome путь: /usr/bin/google-chrome"

                                      # Создаем скрипт запуска тестов с Xvfb
                                      cat > /tmp/run-ui-tests.sh << 'SCRIPT_EOF'
              #!/bin/bash
              set -e

              # Экспортируем переменные
              export CHROME_BIN="/usr/bin/google-chrome"
              export CHROMEDRIVER_PATH="$1"
              export DISPLAY=:99

              echo "Запускаем Xvfb на display $DISPLAY..."
              Xvfb $DISPLAY -screen 0 1920x1080x24 -ac &
              XVFB_PID=$!

              # Даем Xvfb время на запуск
              sleep 3

              echo "Запускаем UI тесты..."
              mvn test -Dtest=*UiTest* \
                  -Dwebdriver.chrome.driver="$CHROMEDRIVER_PATH" \
                  -Dchrome.binary.path="$CHROME_BIN" \
                  -Dchrome.args="--no-sandbox,--disable-dev-shm-usage,--disable-gpu,--window-size=1920,1080,--headless" \
                  -Dtest.failure.ignore=false

              # Сохраняем код возврата
              TEST_EXIT_CODE=$?

              echo "Останавливаем Xvfb..."
              kill $XVFB_PID 2>/dev/null || true

              exit $TEST_EXIT_CODE
              SCRIPT_EOF

                                      chmod +x /tmp/run-ui-tests.sh

                                      # Запускаем тесты
                                      /tmp/run-ui-tests.sh "$DRIVER_PATH"
                                  '''
                              }
                          }
                          post {
                              always {
                                  junit 'target/surefire-reports/**/*.xml'

                                  // Allure отчеты только если есть результаты
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
    }
}