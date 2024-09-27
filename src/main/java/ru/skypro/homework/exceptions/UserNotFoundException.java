package ru.skypro.homework.exceptions;

public class UserNotFoundException extends RuntimeException {

    private final String username;

    public UserNotFoundException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return String.format("User with username: '%s' not found!", username);
    }
}
