package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    public Comments getComments(int adId) {

        Comment comment = new Comment();
        comment.setAuthor(1);
        comment.setAuthorImage("/images/avatar.jpg");
        comment.setAuthorFirstName("Иван");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setPk(100);
        comment.setText("Отличное объявление!");

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

        Comments comments = new Comments();
        comments.setCount(commentList.size());
        comments.setResults(commentList);

        return comments;
    }

    public Comment addComment(int adId, CreateOrUpdateComment createComment) {

        Comment comment = new Comment();
        comment.setAuthor(1);
        comment.setAuthorImage("/images/avatar.jpg");
        comment.setAuthorFirstName("Иван");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setPk(200);
        comment.setText(createComment.getText());

        return comment;
    }

    public void deleteComment(int adId, int commentId) {
        // пока заглушка
    }

    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment updateComment) {

        Comment comment = new Comment();
        comment.setAuthor(1);
        comment.setAuthorImage("/images/avatar.jpg");
        comment.setAuthorFirstName("Иван");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setPk(commentId);
        comment.setText(updateComment.getText());

        return comment;
    }
}