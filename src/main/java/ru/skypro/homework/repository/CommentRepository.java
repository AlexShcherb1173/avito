package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByAdvertisementId(Long adId);  // Все комментарии объявления
    Optional<Comment> findByIdAndAuthorId(Long id, Long authorId); // Для проверки владельца
}
