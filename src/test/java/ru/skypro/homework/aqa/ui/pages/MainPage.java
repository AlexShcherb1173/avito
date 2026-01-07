package ru.skypro.homework.aqa.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Page Object для главной страницы приложения Ads-Online.
 * Содержит элементы и методы для взаимодействия с главной страницей.
 */
public class MainPage {
    // --- Header ---
    private final SelenideElement header = $(".header");
    private final SelenideElement logoImg = $(".header__img");
    private final SelenideElement loginButton = $(".button-link__text");

    // --- Main ---
    private final SelenideElement main = $(".main");
    private final SelenideElement promoTitle = $(".promo__title");
    private final SelenideElement promoSubtitle = $(".promo__subtitle");
    private final SelenideElement searchInput = $(".searchForm__input");
    private final SelenideElement errorMessage = $("p.error-paragraph");

    // --- Footer ---
    private final SelenideElement footer = $(".footer");
    private final SelenideElement copyrightText = $(".footer__copyright");

    // --- POPUP NAVIGATION ЛОКАТОРЫ ---
    private final SelenideElement popupNavigation = $(".popupNavigation");

    @Step("Проверить загрузку главной страницы")
    public MainPage verifyIsLoaded() {
        header.shouldBe(visible);
        main.shouldBe(visible);
        footer.shouldBe(visible);
        popupNavigation.shouldBe(hidden);
        return this;
    }

    // --- HEADER МЕТОДЫ ---
    @Step("Получить текст кнопки входа")
    public String getLoginButtonText() {
        return loginButton.getText();
    }

    @Step("Проверить, что кнопка входа кликабельна")
    public boolean isLoginButtonClickable() {
        return loginButton.is(enabled);
    }

    @Step("Нажать на кнопку входа")
    public LoginPage clickLoginButton() {
        loginButton.click();
        return new LoginPage();
    }

    @Step("Проверить, что логотип кликабельный")
    public boolean isLogoClickable() {
        return logoImg.is(visible) && logoImg.is(enabled);
    }

    // --- MAIN CONTENT МЕТОДЫ ---
    @Step("Получить заголовок промо-блока")
    public String getPromoTitle() {
        return promoTitle.getText();
    }

    @Step("Получить подзаголовок промо-блока")
    public String getPromoSubtitle() {
        return promoSubtitle.getText();
    }

    @Step("Проверить активность поля поиска")
    public boolean isSearchInputEnabled() {
        return searchInput.isEnabled();
    }

    @Step("Получить placeholder поля поиска")
    public String getSearchPlaceholder() {
        return searchInput.getAttribute("placeholder");
    }

    @Step("Получить текст сообщения об ошибке")
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    // --- FOOTER МЕТОДЫ ---
    @Step("Получить текст копирайта в футере")
    public String getCopyrightText() {
        return copyrightText.getText();
    }
}