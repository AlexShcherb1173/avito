package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findByAdId(Integer adId);

    Optional<CommentEntity> findByIdAndAdId(Integer id, Integer adId);

    @Query("SELECT c FROM CommentEntity c WHERE c.ad.id = :adId ORDER BY c.createdAt DESC")
    List<CommentEntity> findAllByAdIdOrderByCreatedAtDesc(@Param("adId") Integer adId);

    void deleteByIdAndAdId(Integer id, Integer adId);

    boolean existsByIdAndAdId(Integer id, Integer adId);

    @Query("SELECT c FROM CommentEntity c WHERE c.author.email = :username")
    List<CommentEntity> findByAuthorEmail(@Param("username") String username);
}