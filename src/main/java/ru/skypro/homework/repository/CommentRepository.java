package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.dto.CommentDTO;

@Repository
public interface CommentRepository extends JpaRepository<CommentDTO, Long> {

}
