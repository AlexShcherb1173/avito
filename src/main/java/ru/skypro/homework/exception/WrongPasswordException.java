package ru.skypro.homework.exception;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super("Wrong old password");
    }

}
