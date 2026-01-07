package ru.skypro.homework.aqa.ui.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.aqa.annotations.UiTest;
import ru.skypro.homework.aqa.ui.UiTestBase;
import ru.skypro.homework.aqa.ui.pages.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.skypro.homework.aqa.utils.TagDefinitions.*;

@UiTest
@DisplayName("Тесты главной страницы")
@Epic("UI Тесты")
@Tag(MAIN_PAGE)
public class MainPageTest extends UiTestBase {

    private MainPage mainPage;

    @BeforeEach
    void setUp() {
        mainPage = new MainPage();
        mainPage.verifyIsLoaded();
    }

    //HEADER
    @Test
    @DisplayName("Кнопка входа содержит правильный текст")
    @Description("Проверка, что кнопка входа на главной странице содержит корректный текст 'Войти'")
    @Tag(REGRESSION)
    void loginButtonShouldHaveCorrectText() {
        assertEquals("Войти", mainPage.getLoginButtonText(),
                "Текст кнопки входа должен быть 'Войти'");
    }

    @Test
    @DisplayName("Кнопка входа кликабельна")
    @Description("Проверка, что кнопка входа на главной странице активна и доступна для клика")
    @Tag(SMOKE)
    void loginButtonShouldBeClickable() {
        assertTrue(mainPage.isLoginButtonClickable(),
                "Кнопка входа должна быть кликабельна");
    }

    @Test
    @DisplayName("При нажатии на кнопку входа должна загрузится страница авторизации")
    @Description("Проверка, что клик по кнопке входа корректно перенаправляет на страницу авторизации")
    @Tag(SMOKE)
    void loginButtonShouldLoadAuthorizationPage() {
        mainPage.clickLoginButton()
                .verifyIsLoaded();
    }

    @Test
    @DisplayName("Логотип должен быть кликабельным")
    @Description("Проверка, что логотип на главной странице является кликабельным элементом")
    @Tag(REGRESSION)
    void logoShouldBeClickable() {
        assertTrue(mainPage.isLogoClickable());
    }

    //MAIN
    @Test
    @DisplayName("Промо-блок содержит правильный текст")
    @Description("Проверка, что заголовок промо-блока на главной странице содержит текст 'Ads-Online'")
    @Tag(REGRESSION)
    void promoTitleShouldHaveCorrectText() {
        assertEquals("Ads-Online", mainPage.getPromoTitle());
    }

    @Test
    @DisplayName("Подзаголовок промо-блока содержит правильный текст")
    @Description("Проверка, что подзаголовок промо-блока содержит текст 'Лучшая платформа для продажи вещей'")
    @Tag(REGRESSION)
    void promoSubtitleShouldHaveCorrectText() {
        assertEquals("Лучшая платформа для продажи вещей", mainPage.getPromoSubtitle());
    }

    @Test
    @DisplayName("Поле поиска активно")
    @Description("Проверка, что поле поиска на главной странице активно и доступно для ввода")
    @Tag(SMOKE)
    void searchInputShouldBeActive() {
        assertTrue(mainPage.isSearchInputEnabled());
    }

    @Test
    @DisplayName("Placeholder поля поиска содержит правильный текст")
    @Description("Проверка, что placeholder поля поиска содержит текст 'Поиск'")
    @Tag(REGRESSION)
    void searchPlaceholderShouldHaveCorrectText() {
        assertEquals("Поиск", mainPage.getSearchPlaceholder());
    }

    @Test
    @DisplayName("Сообщение об ошибке содержит правильный текст")
    @Description("Проверка, что сообщение об ошибке при пустом результате поиска содержит корректный текст")
    @Tag(REGRESSION)
    void errorMessageShouldHaveCorrectText() {
        assertEquals("По вашему запросу ничего не найдено", mainPage.getErrorMessage());
    }

    //FOOTER
    @Test
    @DisplayName("Копирайт содержит правильный текст")
    @Description("Проверка, что текст копирайта в футере содержит актуальную информацию с годом 2026")
    @Tag(REGRESSION)
    void copyrightTextShouldHaveCorrectText() {
        assertEquals("© Skypro 2026. All rights reserved.", mainPage.getCopyrightText());
    }
}