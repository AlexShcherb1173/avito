package ru.skypro.homework.impl;

import ru.skypro.homework.service.RegisterService;

public class RegisterServiceImpl implements RegisterService {
    public static String getMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[2].getMethodName();
    }
}