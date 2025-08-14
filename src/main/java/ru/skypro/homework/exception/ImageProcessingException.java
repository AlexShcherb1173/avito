package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое при ошибках обработки изображений
 */
public class ImageProcessingException extends RuntimeException {
    public ImageProcessingException() {
        super();
    }

    public ImageProcessingException(String message) {
        super(message);
    }

    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageProcessingException(Throwable cause) {
        super(cause);
    }
}