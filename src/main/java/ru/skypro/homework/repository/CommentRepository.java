package ru.skypro.homework.repository;

import org.springframework.stereotype.Repository;
import ru.skypro.homework.enity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAdId(Long adId);
}
