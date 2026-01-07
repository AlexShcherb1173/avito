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

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true));

        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1200";
        Configuration.baseUrl = "http://localhost:3000";

        Configuration.pageLoadStrategy = "normal";
        Configuration.screenshots = false;
        Configuration.savePageSource = false;
    }

    @BeforeEach
    void openBrowser() {
        open("/");

        clearBrowserCookies();
        clearBrowserLocalStorage();
        executeJavaScript("sessionStorage.clear();");
    }

    @AfterAll
    static void tearDownAll() {
        Selenide.closeWebDriver();
    }
}
