package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис для работы с объявлениями.
 * Включает логику создания, редактирования и проверки прав доступа к объявлениям.
 */
@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Проверка, может ли пользователь редактировать объявление.
     * Пользователь может редактировать только свои объявления, или если он администратор.
     *
     * @param adId идентификатор объявления
     * @param authentication объект аутентификации, содержащий данные о текущем пользователе
     * @return true, если пользователь может редактировать объявление, иначе false
     */
    public boolean canEditAd(Integer adId, Authentication authentication) {
        Ad ad = adRepository.findById(adId).orElseThrow();
        User user = userRepository.findByEmail(authentication.getName());
        return ad.getAuthor().getId().equals(user.getId()) || user.getRole().equals(User.Role.ADMIN);
    }

    /**
     * Создает новое объявление.
     * Присваивает объявлению текущего авторизованного пользователя как автора.
     *
     * @param ad объект объявления
     * @param authentication объект аутентификации, содержащий данные о текущем пользователе
     * @return созданное объявление
     */
    public Ad createAd(Ad ad, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        ad.setAuthor(user);
        return adRepository.save(ad);
    }
}
