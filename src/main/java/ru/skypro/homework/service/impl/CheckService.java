package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
/**
 * Сервис валидации для проверки различных типов данных.
 */
@Service
public class CheckService {

    /**
     * Проверяет, соответствует ли длина строки заданным минимальным и максимальным значениям.
     */
    public static boolean checkLength(String name, int min, int max) {
        return name.length() >= min && name.length() <= max;
    }
    /**
     * Проверяет, находится ли цена в заданном диапазоне значений.
     */
    public static boolean checkPrice(Integer price, int min, int max) {
        return price != null && price >= min && price <= max;
    }
    /**
     * Проверяет, соответствует ли строка формату электронного адреса.
     */
    public static boolean checkUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", username);
    }
    /**
     * Проверяет, состоит ли строка только из букв (латиница и кириллица).
     */
    public static boolean checkSymbol(String name) {
        return Pattern.matches("[a-zA-Zа-яА-ЯёЁ]+", name);
    }
    /**
     * Проверяет, соответствует ли строка формату телефонного номера.
     */
    public static boolean checkPhone(String phone) {
        return Pattern.matches("^\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$", phone);
    }
}
