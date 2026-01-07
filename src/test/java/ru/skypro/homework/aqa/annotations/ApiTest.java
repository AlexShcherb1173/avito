package ru.skypro.homework.aqa.annotations;

import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

import static ru.skypro.homework.aqa.utils.TagDefinitions.API;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Epic("API Тесты")
@Owner("API QA Team")
@Severity(SeverityLevel.CRITICAL)
@Tag(API)
public @interface ApiTest { }
