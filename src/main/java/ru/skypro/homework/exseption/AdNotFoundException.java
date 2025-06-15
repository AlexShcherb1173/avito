package ru.skypro.homework.exseption;

public class AdNotFoundException extends RuntimeException {

    public AdNotFoundException(String message) {
        super(message);
    }
}
