package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.User;

import java.util.Optional;
/**
 * Репозиторий для работы с сущностями пользователей.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Находит пользователя по его логину.
     */
    Optional<User> findByUsername(String username);
}