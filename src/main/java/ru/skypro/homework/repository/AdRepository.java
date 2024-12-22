package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ad;

import java.util.List;
/**
 * Репозиторий для работы с сущностями объявлений.
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    /**
     * Находит список объявлений по идентификатору пользователя.
     */
    List<Ad> findByUserId(Integer userId);
}