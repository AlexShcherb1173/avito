package ru.skypro.homework.aqa.api.specs;


import com.github.javafaker.Faker;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseApiTest {

    @LocalServerPort
    private int port;

    protected Faker faker;

    private static final String BASE_URI = "http://localhost:";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URI + port;

        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );

        faker = new Faker();
    }

    protected RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }
}
