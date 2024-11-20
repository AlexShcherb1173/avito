package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.model.CommentModel;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Integer> {

    List<CommentModel> findByAdAdId(Integer adId);
}
