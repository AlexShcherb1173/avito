package ru.avito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avito.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAdId(Integer adId);
}