package ru.skypro.homework.roles;

public interface RoleAuthority {
    String ADMIN = "hasRole('ADMIN')";
    String USER = "hasRole('USER')";
    String ALL = "permitAll()";
}