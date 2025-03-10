package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Integer> {

    /**
     * Находит все объявления, созданные пользователем с указанным email.
     *
     * @param email email пользователя
     * @return список объявлений
     */
    List<Ad> findByUserEmail(String email);

    /**
     * Находит объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return объявление (если найдено)
     */
    Optional<Ad> findById(Integer id);
}