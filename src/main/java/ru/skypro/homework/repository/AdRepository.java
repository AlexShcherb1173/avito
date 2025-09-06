package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

import java.util.List;

// Репозиторий для работы с объявлениями.
// Предоставляет методы для поиска объявлений по различным критериям.

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    // Найти все объявления по автору.
    // @param author автор объявлений
    // @return список объявлений автора

    List<Ad> findByAuthor(User author);

    // Найти все объявления по ID автора.
    // @param authorId ID автора
    // @return список объявлений автора

    List<Ad> findByAuthorId(Long authorId);

    // Найти объявления по заголовку (без учета регистра).
    // @param title часть заголовка для поиска
    // @return список найденных объявлений

    List<Ad> findByTitleContainingIgnoreCase(String title);

    // Найти объявления по описанию (без учета регистра).
    // @param description часть описания для поиска
    // @return список найденных объявлений

    List<Ad> findByDescriptionContainingIgnoreCase(String description);

    // Найти объявления по заголовку или описанию (без учета регистра).
    // @param title часть заголовка для поиска
    // @param description часть описания для поиска
    // @return список найденных объявлений

    List<Ad> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}