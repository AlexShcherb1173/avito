package ru.skypro.homework.exception;

public class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException() {
        super("Object not found");
    }

}
