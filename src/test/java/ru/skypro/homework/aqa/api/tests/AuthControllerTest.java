package ru.skypro.homework.aqa.api.tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ru.skypro.homework.aqa.annotations.ApiTest;
import ru.skypro.homework.aqa.api.specs.BaseApiTest;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterUserDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.skypro.homework.aqa.utils.TagDefinitions.*;

@ApiTest
@Epic("API Тесты")
@DisplayName("Интеграционные тесты контроллера аутентификации и регистрации пользователей")
public class AuthControllerTest extends BaseApiTest {

    @Autowired
    private UserRepository userRepository;

    private static final String LOGIN_PATH = "/login";
    private static final String REGISTER_PATH = "/register";

    @Test
    @Tag(POSITIVE)
    @Tag(SMOKE)
    @DisplayName("Регистрация нового пользователя с валидными данными")
    @Description("Тест проверяет успешную регистрацию нового пользователя с валидными данными")
    void register_newUserWithValidData_returnsCreated() {
        RegisterUserDto request = createUserTest();
        String userName = request.getUsername();

        Allure.step("Подготовка тестовых данных", () -> Allure.addAttachment("Тестовый пользователь",
                "application/json",
                request.toString()));

        try {
            Allure.step("Отправка запроса на регистрацию", () -> {
                given()
                        .spec(getRequestSpec())
                        .body(request)
                        .when()
                        .post(REGISTER_PATH)
                        .then()
                        .statusCode(HttpStatus.CREATED.value());
            });

            Allure.step("Проверка сохранения пользователя в БД", () -> {
                Optional<User> savedUser = userRepository.findByUsername(userName);

                assertTrue(savedUser.isPresent(), "Пользователь должен быть сохранен в БД");
                assertEquals(request.getFirstName(), savedUser.get().getFirstName(),
                        "Имя должно совпадать");
                assertEquals(request.getLastName(), savedUser.get().getLastName(),
                        "Фамилия должна совпадать");
                assertEquals(request.getPhone(), savedUser.get().getPhone(),
                        "Телефон должен совпадать");
            });
        } finally {
            Allure.step("Очистка тестовых данных", () ->
                    userRepository.findByUsername(userName).ifPresent(userRepository::delete));
        }
    }

    @Test
    @Tag(NEGATIVE)
    @Tag(REGRESSION)
    @DisplayName("Регистрация пользователя с уже существующим именем")
    @Description("Тест проверяет попытку регистрации пользователя с уже существующим именем")
    void register_secondUserWithValidData_returnsBadRequest() {
        RegisterUserDto request = createUserTest();
        String userName = request.getUsername();

        Allure.step("Подготовка: регистрация первого пользователя", () -> {
            given()
                    .spec(getRequestSpec())
                    .body(request)
                    .when()
                    .post(REGISTER_PATH)
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        });

        try {
            Allure.step("Попытка регистрации второго пользователя с тем же именем", () -> {
                given()
                        .spec(getRequestSpec())
                        .body(request)
                        .when()
                        .post(REGISTER_PATH)
                        .then()
                        .body("status", equalTo(400))
                        .body("error", equalTo("Bad Request"));
            });
        } finally {
            Allure.step("Очистка тестовых данных", () ->
                    userRepository.findByUsername(userName).ifPresent(userRepository::delete));
        }
    }

    @Test
    @Tag(POSITIVE)
    @Tag(SMOKE)
    @DisplayName("Успешная авторизация зарегистрированного пользователя")
    @Description("Тест проверяет успешную авторизацию зарегистрированного пользователя")
    void login_registeredUser_returnsOk() {
        RegisterUserDto request = createUserTest();

        Allure.step("Регистрация тестового пользователя", () -> {
            given()
                    .spec(getRequestSpec())
                    .body(request)
                    .when()
                    .post(REGISTER_PATH)
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        });

        try {
            LoginDto loginDto = createLoginDto(request);

            Allure.step("Авторизация с валидными данными", () -> {
                given()
                        .spec(getRequestSpec())
                        .body(loginDto)
                        .when()
                        .post(LOGIN_PATH)
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .body("$", hasKey("token"))
                        .body("token", notNullValue())
                        .body("token", not(emptyString()))
                        .body("token", matchesPattern("^[^.]+\\.[^.]+\\.[^.]+$"))
                        .time(lessThan(3000L));
            });
        } finally {
            Allure.step("Очистка тестовых данных", () ->
                    userRepository.findByUsername(request.getUsername()).ifPresent(userRepository::delete));
        }
    }

    @Test
    @Tag(NEGATIVE)
    @Tag(REGRESSION)
    @DisplayName("Авторизация с неверным паролем")
    @Description("Тест проверяет попытку авторизации с неверным паролем")
    void login_withWrongPassword_returnsInternalServerError() {
        RegisterUserDto request = createUserTest();

        Allure.step("Регистрация тестового пользователя", () -> {
            given()
                    .spec(getRequestSpec())
                    .body(request)
                    .when()
                    .post(REGISTER_PATH)
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        });

        try {
            LoginDto loginDto = new LoginDto();
            Allure.step("Подготовка данных с неверным паролем", () -> {
                String invalidPassword = faker.internet()
                        .password(10, 20, true, true, true);
                loginDto.setUsername(request.getUsername());
                loginDto.setPassword(invalidPassword);

                Allure.addAttachment("Неверный пароль",
                        "text/plain",
                        "Сгенерированный неверный пароль: " + invalidPassword);
            });

            Allure.step("Попытка авторизации с неверным паролем", () -> {
                given()
                        .spec(getRequestSpec())
                        .body(loginDto)
                        .when()
                        .post(LOGIN_PATH)
                        .then()
                        .statusCode(500)
                        .body("status", equalTo(500))
                        .body("error", equalTo("Internal Server Error"));
            });
        } finally {
            Allure.step("Очистка тестовых данных", () ->
                    userRepository.findByUsername(request.getUsername()).ifPresent(userRepository::delete));
        }
    }

    @Test
    @Tag(NEGATIVE)
    @Tag(REGRESSION)
    @DisplayName("Авторизация незарегистрированного пользователя")
    @Description("Тест проверяет попытку авторизации незарегистрированного пользователя")
    void login_unregisteredUser_returnsInternalServerError() {
        RegisterUserDto request = createUserTest();
        LoginDto loginDto = createLoginDto(request);

        Allure.step("Проверка отсутствия пользователя в БД", () ->
                assertFalse(userRepository.findByUsername(request.getUsername()).isPresent(),
                        "Пользователь не должен существовать в БД"));

        Allure.step("Попытка авторизации незарегистрированного пользователя", () -> {
            given()
                    .spec(getRequestSpec())
                    .body(loginDto)
                    .when()
                    .post(LOGIN_PATH)
                    .then()
                    .statusCode(500)
                    .body("status", equalTo(500))
                    .body("error", equalTo("Internal Server Error"));
        });
    }

    @Step("Создание тестового пользователя")
    private RegisterUserDto createUserTest() {
        final String testUsername = faker.internet().emailAddress();
        final String testPassword = faker.internet()
                .password(10, 20, true, true, true);
        final String testFirstName = faker.name().firstName();
        final String testLastName = faker.name().lastName();
        final String testPhone = "+7" + faker.number().digits(10);

        RegisterUserDto request = new RegisterUserDto();
        request.setUsername(testUsername);
        request.setPassword(testPassword);
        request.setFirstName(testFirstName);
        request.setLastName(testLastName);
        request.setPhone(testPhone);
        request.setRole(Role.USER);
        return request;
    }

    @Step("Создание DTO для логина из данных регистрации")
    private LoginDto createLoginDto(RegisterUserDto request) {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(request.getUsername());
        loginDto.setPassword(request.getPassword());
        return loginDto;
    }
}
