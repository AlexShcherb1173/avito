package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

@Service
public class CommentService {
    public Comments getComments(Integer id) {
        return null;
    }
    public Comment addComment(Integer id, CreateOrUpdateComment createCommentDto) {
        return null;
    }

    public boolean hasCommentAccess(Integer commentId) {
        return false;
    }

    public void deleteComment(Integer commentId) {

    }

    public Comment updateComment(Integer commentId, Comment commentDto) {

        return commentDto;
    }
}
