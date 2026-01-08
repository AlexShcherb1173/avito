package ru.skypro.homework.aqa.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.*;

public class UiTestBase {

    @BeforeAll
    static void setUpAll() {
        System.out.println("=== НАСТРОЙКА UI ТЕСТОВ ===");
        System.out.println("User: " + System.getProperty("user.name"));
        System.out.println("OS: " + System.getProperty("os.name"));

        // Проверяем, используем ли мы remote WebDriver (Selenium в Docker)
        String remoteUrl = System.getProperty("selenide.remote");
        boolean useRemote = remoteUrl != null && !remoteUrl.isEmpty();

        if (useRemote) {
            System.out.println("Используем Selenium в Docker: " + remoteUrl);
            Configuration.remote = remoteUrl;
            Configuration.browser = "chrome";

            // При использовании --network="host" в Docker, localhost внутри контейнера = localhost на хосте
            Configuration.baseUrl = "http://localhost:3000";
        } else {
            // Локальная разработка
            System.out.println("Используем локальный WebDriver");
            Configuration.browser = "chrome";
            Configuration.browserSize = "1920x1200";
            Configuration.baseUrl = "http://localhost:3000";
        }

        Configuration.timeout = 30000; // 30 секунд
        Configuration.pageLoadStrategy = "normal";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;

        // Allure
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false)
                .includeSelenideSteps(true));

        System.out.println("Конфигурация:");
        System.out.println("- Remote: " + Configuration.remote);
        System.out.println("- Browser: " + Configuration.browser);
        System.out.println("- Base URL: " + Configuration.baseUrl);
        System.out.println("- Timeout: " + Configuration.timeout);
    }

    @BeforeEach
    void openBrowser() {
        System.out.println("Открываем браузер...");
        try {
            open("/");
            System.out.println("Браузер успешно открыт");
        } catch (Exception e) {
            System.err.println("Ошибка при открытии браузера: " + e.getMessage());
            throw e;
        }
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Закрываем WebDriver...");
        Selenide.closeWebDriver();
    }
}
