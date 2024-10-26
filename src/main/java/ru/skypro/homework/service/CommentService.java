package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.entity.Comment;

public interface CommentService {

    CommentDTO add(Comment comment);

    CommentDTO getComment(Long id);

    void delete(Comment comment);

    CommentDTO update(Comment comment);
}
