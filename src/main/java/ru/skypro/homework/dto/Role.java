package ru.skypro.homework.dto;

public enum Role {
    USER, ADMIN;

    @Override
    public String toString() {
        return this.name();
    }
}