package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAdId(Integer adId);

    void deleteByIdAndAdId(Integer commentId, Integer adId);

    Optional<Comment> findByIdAndAdId(Integer commentId, Integer adId);
}
