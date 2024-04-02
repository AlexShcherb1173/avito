package ru.skypro.homework.service;

import ru.skypro.homework.entity.User;

public interface UserOrAdminService {
    boolean isUser(String email, User ownerAds);

    boolean isAdmin(String email);

    boolean isUserOrAdmin(String email, User ownerAds);
}
