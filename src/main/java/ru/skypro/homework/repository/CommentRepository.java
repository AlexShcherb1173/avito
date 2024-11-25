package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.model.CommentEntity;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query(value = "SELECT * FROM comment_entity WHERE ad_id=:id", nativeQuery = true)
    List<CommentEntity> findAllCommentsByAdId(long id);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM comment_entity WHERE ad_id=:id)", nativeQuery = true)
    boolean existsCommentAdByAdId(int id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM comment_entity WHERE ad_id=:adId AND comment_id=:commentId", nativeQuery = true)
    void deleteCommentByCommentIdAndAdId(int adId, int commentId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE comment_entity SET text=:text WHERE ad_id=:adId AND comment_id=:commentId", nativeQuery = true)
    void updateCommentTextByCommentIdAndAdId(int adId, int commentId, String text);

    @Query(value = "SELECT * FROM comment_entity WHERE ad_id=:adId AND comment_id=:commentId", nativeQuery = true)
    CommentEntity findCommentByCommentIdAndAdId(int adId, int commentId);
}
