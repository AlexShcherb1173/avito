package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

import java.util.List;

/**
 * Репозиторий для работы с объявлениями (Ad).
 * Наследуется от {@link JpaRepository}, что позволяет использовать стандартные CRUD-методы.
 */
public interface AdRepository extends JpaRepository<Ad, Integer> {

    /**
     * Метод для поиска всех объявлений, созданных указанным пользователем.
     *
     * @param user Пользователь, чьи объявления нужно найти.
     * @return Список объявлений, принадлежащих указанному пользователю.
     */
    List<Ad> findByAuthor(User user);
}
