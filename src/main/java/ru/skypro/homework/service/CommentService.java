package ru.skypro.homework.service;

import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Comments;

public interface CommentService {

    Comments getComments(Integer adId);
}
