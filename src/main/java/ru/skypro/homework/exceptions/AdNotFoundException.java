package ru.skypro.homework.exceptions;

public class AdNotFoundException extends RuntimeException{

    public AdNotFoundException(String str)  {
        super(str);
    }

//    @Override
//    public String getMessage() {
//        return "User with id = %s not found".formatted(getMes());
//    }
}
