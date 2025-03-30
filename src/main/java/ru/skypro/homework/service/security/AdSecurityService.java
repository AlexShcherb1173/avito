package ru.skypro.homework.service.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.AdRepository;

/**
 * Сервис безопасности для работы с объявлениями.
 */
@Service
public class AdSecurityService {
    private final AdRepository adRepository;

    /**
     * Конструктор AdSecurityService.
     *
     * @param adRepository репозиторий объявлений
     */
    public AdSecurityService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    /**
     * Проверяет, является ли текущий пользователь владельцем объявления.
     *
     * @param adId идентификатор объявления
     * @return true, если пользователь является владельцем
     */
    public boolean isOwner(Integer adId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        return ad.getAuthor().getEmail().equals(currentUsername);
    }
}
