package ru.skypro.homework.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

//    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({AdsNotFoundException.class, CommentNotFoundException.class, ImageNotFoundException.class,
            UserNotFoundException.class, WrongPasswordException.class})
    public ResponseEntity<Object> handleCustomException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof AdsNotFoundException || ex instanceof CommentNotFoundException
                || ex instanceof ImageNotFoundException || ex instanceof UserNotFoundException ||
                ex instanceof WrongPasswordException) {
            status = HttpStatus.NOT_FOUND;
        }


        log.error("An error occurred: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(ex.getMessage(), status);
    }
}
