package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserOrAdminService;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrAdminServiceImpl implements UserOrAdminService {
    private final UserRepository userRepository;

    /**
     * @param email почта пользователя
     * @param ownerAds владелец объявления
     * @return
     * Проверяет, является ли пользователь владельцем объявления
     */
    @Override
    public boolean isUser(String email, User ownerAds) {
        if (email.equals(ownerAds.getEmail())) {
            log.info("Пользователь является владельцем объявления!");

            return true;
        } else {
            log.info("Пользователь не является владельцем объявления!");
            return false;
        }
    }

    /**
     * @param email почта пользователя
     * @return
     * Проверяет, является ли пользователь администратором
     */
    @Override
    public boolean isAdmin(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        if (user.getRole() == (Role.ADMIN)) {
            log.info("Пользователь является администратором!");
            return true;
        } else {
            log.info("Пользователь не является администратором!");
            return false;
        }
    }

    /**
     * @param email почта пользователя
     * @param ownerAds владелец объявления
     * @return
     * Проверяет, является ли пользователь владельцем объявления или администратором
     */
    @Override
    public boolean isUserOrAdmin(String email, User ownerAds) {
        return isUser(email, ownerAds) || isAdmin(email);
    }
}
