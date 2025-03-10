package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.User;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями (User).
 * Наследуется от {@link JpaRepository}, что предоставляет стандартные методы для работы с базой данных.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Поиск пользователя по имени пользователя (логину).
     *
     * @param username Имя пользователя.
     * @return {@link Optional} с найденным пользователем или пустое значение, если пользователь не найден.
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет существование пользователя с указанным именем.
     *
     * @param username Имя пользователя.
     * @return {@code true}, если пользователь с таким именем существует, иначе {@code false}.
     */
    boolean existsByUsername(String username);

    /**
     * Удаляет пользователя по его имени пользователя.
     *
     * @param username Имя пользователя.
     */
    void deleteByUsername(String username);
}
