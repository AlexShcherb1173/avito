package ru.avito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avito.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByAdIdOrderByCreatedAtAsc(Integer adId);

    Optional<Comment> findByIdAndAdId(Integer commentId, Integer adId);
}