package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.entity.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Находит все комментарии по ID объявления (используется в CommentServiceImpl)
     */
    List<Comment> findAllByAdId(Integer adId);

    /**
     * Находит комментарий по ID комментария и ID объявления (используется в CommentServiceImpl)
     */
    Optional<Comment> findByIdAndAdId(Integer id, Integer adId);

    /**
     * Удаляет комментарий по ID комментария и ID объявления (используется в CommentServiceImpl)
     */
    void deleteByIdAndAdId(Integer id, Integer adId);

    /**
     * Проверяет существование комментария по ID автора
     */
    boolean existsByAuthorId(Integer authorId);

    /**
     * Удаляет все комментарии по ID объявления
     */
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.ad.id = :adId")
    void deleteByAdId(@Param("adId") Integer adId);
}
