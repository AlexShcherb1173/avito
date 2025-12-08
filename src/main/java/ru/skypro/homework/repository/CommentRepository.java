package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link CommentEntity}.
 * Предоставляет методы для доступа к данным комментариев в базе данных.
 * Наследует стандартные CRUD операции от {@link JpaRepository}.
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Находит все комментарии для указанного объявления.
     *
     * @param adId идентификатор объявления
     * @return список комментариев к указанному объявлению
     */
    List<CommentEntity> findByAdId(Integer adId);

    /**
     * Находит комментарий по идентификатору и идентификатору объявления.
     *
     * @param id идентификатор комментария
     * @param adId идентификатор объявления
     * @return {@link Optional} с найденным комментарием или пустой Optional если не найден
     */
    Optional<CommentEntity> findByIdAndAdId(Integer id, Integer adId);

    /**
     * Находит все комментарии для указанного объявления,
     * отсортированные по дате создания в порядке убывания.
     * Использует JPQL запрос для обеспечения точного порядка сортировки.
     *
     * @param adId идентификатор объявления
     * @return список комментариев, отсортированный по дате создания (новые сначала)
     */
    @Query("SELECT c FROM CommentEntity c WHERE c.ad.id = :adId ORDER BY c.createdAt DESC")
    List<CommentEntity> findAllByAdIdOrderByCreatedAtDesc(@Param("adId") Integer adId);

    /**
     * Удаляет комментарий по идентификатору и идентификатору объявления.
     *
     * @param id идентификатор комментария
     * @param adId идентификатор объявления
     */
    void deleteByIdAndAdId(Integer id, Integer adId);

    /**
     * Проверяет существование комментария с указанным идентификатором и идентификатором объявления.
     *
     * @param id идентификатор комментария
     * @param adId идентификатор объявления
     * @return true если комментарий существует, false в противном случае
     */
    boolean existsByIdAndAdId(Integer id, Integer adId);

    /**
     * Находит все комментарии, оставленные указанным пользователем.
     *
     * @param username email пользователя
     * @return список комментариев пользователя
     */
    @Query("SELECT c FROM CommentEntity c WHERE c.author.email = :username")
    List<CommentEntity> findByAuthorEmail(@Param("username") String username);
}