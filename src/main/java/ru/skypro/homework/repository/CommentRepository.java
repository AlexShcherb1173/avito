package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByAdId(int id);

    Comment findByAdIdAndId(int adId, int CommentId);
    void deleteByAdIdAndId(int adId, int CommentId);

}
