package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Collection<CommentEntity> findByAd_Id(long adId);
}
