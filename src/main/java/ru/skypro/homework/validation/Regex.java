package ru.skypro.homework.validation;

/**
 * Класс для хранения условий заполнения мыла и пароля
 */
public class Regex {
    public static final String EMAIL_REGEXP = ".+@.+[.]..+";
    public static final String PHONE_REGEXP = "\\+7\\d{10}";

    private Regex() {
    }
}
