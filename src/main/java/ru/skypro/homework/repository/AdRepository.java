package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link AdEntity}.
 * Предоставляет методы для доступа к данным объявлений в базе данных.
 * Наследует стандартные CRUD операции от {@link JpaRepository}.
 */
@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    /**
     * Находит все объявления указанного автора.
     *
     * @param authorId идентификатор автора
     * @return список объявлений указанного автора
     */
    List<AdEntity> findByAuthorId(Integer authorId);

    /**
     * Находит объявление по идентификатору.
     * Унаследованный метод от {@link JpaRepository}.
     *
     * @param id идентификатор объявления
     * @return {@link Optional} с найденным объявлением или пустой Optional если не найден
     */
    Optional<AdEntity> findById(Integer id);

    /**
     * Находит все объявления указанного пользователя.
     * Использует JPQL запрос для поиска по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список объявлений указанного пользователя
     */
    @Query("SELECT a FROM AdEntity a WHERE a.author.id = :userId")
    List<AdEntity> findAllByUserId(@Param("userId") Integer userId);

    /**
     * Считает количество объявлений указанного пользователя.
     *
     * @param userId идентификатор пользователя
     * @return количество объявлений пользователя
     */
    @Query("SELECT COUNT(a) FROM AdEntity a WHERE a.author.id = :userId")
    Integer countByUserId(@Param("userId") Integer userId);

    /**
     * Находит все объявления указанного пользователя по email.
     *
     * @param username email пользователя
     * @return список объявлений пользователя
     */
    @Query("SELECT a FROM AdEntity a WHERE a.author.email = :username")
    List<AdEntity> findByAuthorEmail(@Param("username") String username);
}