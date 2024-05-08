package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.RoleEntity;
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
}
