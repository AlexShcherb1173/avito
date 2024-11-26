package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;

public interface CommentService {

    CommentDTO add(CommentDTO comment);

    CommentDTO getComment(Long id);

    void delete(CommentDTO comment);

    CommentDTO update(CommentDTO comment);
}
