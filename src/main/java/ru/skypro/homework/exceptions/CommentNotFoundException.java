package ru.skypro.homework.exceptions;

public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException(String str)  {

        super(str);
    }
//    @Override
//    public String getMessage() {
//        return "User with id = %s not found".formatted(getMes());
//    }
}
