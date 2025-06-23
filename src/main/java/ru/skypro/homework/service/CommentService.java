package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.Comments;

public interface CommentService {

    CommentDto addComment(CommentDto commentDTO);
    CommentDto updateComment(Integer id, CommentDto commentDTO);
    void deleteComment(Integer id);
    Comments getAllComments(Integer id);
}
