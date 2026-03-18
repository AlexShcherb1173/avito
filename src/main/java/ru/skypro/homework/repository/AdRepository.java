package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

/**
 * Репозиторий для работы с объявлениями.
 *
 * Предоставляет методы для стандартных CRUD-операций
 * и поиска объявлений по автору.
 */
public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    /**
     * Найти все объявления по идентификатору автора.
     *
     * @param authorId идентификатор автора
     * @return список объявлений автора
     */
    List<AdEntity> findByAuthor_Id(Integer authorId);
}