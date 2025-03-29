package ru.skypro.homework.exception;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(Integer ImageId) {
        super("Image with Id" + ImageId + " not found");
    }
}
