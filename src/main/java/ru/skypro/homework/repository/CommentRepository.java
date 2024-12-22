package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Comment;

import java.util.List;
/**
 * Репозиторий для работы с сущностями комментария.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    /**
     * Находит список комментариев по идентификатору объявления.
     */
List<Comment> findByAdId(Integer adId);
}
