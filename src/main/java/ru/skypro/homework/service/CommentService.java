package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private List<Comment> commentList = new ArrayList<>(); // Хранение комментариев в памяти (можно заменить на БД)
    private int currentId = 1; // Счетчик ID для новых комментариев

    public Comments getCommentsForAd(Integer adId) {
        Comments comments = new Comments();
        // Фильтрация комментариев по ID объявления
        List<Comment> adComments = new ArrayList<>();
        for (Comment comment : commentList) {
            if (comment.getAdId().equals(adId)) {
                adComments.add(comment);
            }
        }
        comments.setComments(adComments);
        return comments;
    }

    public Comment addComment(Integer adId, CreateOrUpdateComment commentDto) {
        Comment newComment = new Comment();
        newComment.setPk(currentId++);
        newComment.setAdId(adId);
        newComment.setText(commentDto.getText());

        // Сохранение нового комментария в списке
        commentList.add(newComment);
        return newComment;
    }

    public void updateComment(Integer adId, Integer commentId, CreateOrUpdateComment commentDto) {
        Comment existingComment = getCommentById(commentId);
        if (!existingComment.getPk().equals(adId)) {
            throw new RuntimeException("Комментарий не принадлежит данному объявлению");
        }
        existingComment.setText(commentDto.getText());
    }

    public void deleteComment(Integer adId, Integer commentId) {
        Comment commentToDelete = getCommentById(commentId);
        if (!commentToDelete.getPk().equals(adId)) {
            throw new RuntimeException("Комментарий не принадлежит данному объявлению");
        }
        commentList.remove(commentToDelete);
    }

    private Comment getCommentById(Integer id) {
        Optional<Comment> commentOptional = commentList.stream().filter(comment -> comment.getPk().equals(id)).findFirst();
        if (commentOptional.isPresent()) {
            return commentOptional.get();
        }
        throw new RuntimeException("Комментарий не найден");
    }
}