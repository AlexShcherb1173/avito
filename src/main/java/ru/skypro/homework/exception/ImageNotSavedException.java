package ru.skypro.homework.exception;

public class ImageNotSavedException extends RuntimeException {
    public ImageNotSavedException(String message) {
        super("Image not save");
    }
}
