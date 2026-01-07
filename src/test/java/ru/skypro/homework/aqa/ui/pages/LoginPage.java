package ru.skypro.homework.aqa.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

/**
 * Упрощенный Page Object для страницы авторизации приложения Ads-Online.
 * Содержит элементы и методы для проверки кнопки входа главной страницы.
 */
public class LoginPage {

    private final SelenideElement header = $(".header");
    private final SelenideElement main = $("main.form");
    private final SelenideElement footer = $(".footer");

    @Step("Проверить загрузку страницы авторизации")
    public LoginPage verifyIsLoaded() {
        header.shouldBe(visible);
        main.shouldBe(visible);
        footer.should(exist);
        return this;
    }
}