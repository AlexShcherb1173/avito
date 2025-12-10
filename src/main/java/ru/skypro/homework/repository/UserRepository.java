package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link UserEntity}.
 * Предоставляет методы для доступа к данным пользователей в базе данных.
 * Наследует стандартные CRUD операции от {@link JpaRepository}.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Находит пользователя по email.
     *
     * @param email email пользователя для поиска
     * @return {@link Optional} с найденным пользователем или пустой Optional если не найден
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Проверяет существование пользователя с указанным email.
     *
     * @param email email для проверки
     * @return true если пользователь существует, false в противном случае
     */
    boolean existsByEmail(String email);

    /**
     * Находит пользователя по идентификатору.
     * Унаследованный метод от {@link JpaRepository}.
     *
     * @param id идентификатор пользователя
     * @return {@link Optional} с найденным пользователем или пустой Optional если не найден
     */
    Optional<UserEntity> findById(Integer id);

    /**
     * Находит пользователя по email с использованием JPQL запроса.
     * Альтернативный метод для {@link #findByEmail(String)}.
     *
     * @param email email пользователя для поиска
     * @return {@link Optional} с найденным пользователем или пустой Optional если не найден
     */
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findUserByEmail(@Param("email") String email);
}