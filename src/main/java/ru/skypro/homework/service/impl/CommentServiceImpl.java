package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
    }

    public List<CommentModel> getCommentsByAdId(int adId) {
        List<CommentModel> comments = commentRepository.findByAdAdId(adId);
        if (comments.isEmpty()) {
            throw new NotFoundException("Комментарии не найдены для объявления с ID: " + adId);
        }
        return comments;
    }

    public CommentModel addCommentToAd(Integer adId, CommentModel comment) {
        AdModel ad = adRepository.findById(adId).orElseThrow(() -> new
                EntityNotFoundException("Объявление с " + adId + " не найдено."));
        comment.setAd(ad);
        return commentRepository.save(comment);
    }

    public void removeComment(Integer adId, Integer commentId, String username) {
        if (!adRepository.existsById(adId)) {
            throw new EntityNotFoundException("Объявление с " + adId + " не найдено.");
        }
        CommentModel comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ID " + commentId + " не найден"));
        if (!comment.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("У вас нет прав для удаления этого комментария.");
        }
        commentRepository.delete(comment);
    }

    public void updateComment(Integer adId, Integer commentId, CommentModel commentModel) {
        if (!adRepository.existsById(adId)) {
            throw new EntityNotFoundException("Объявление с " + adId + " не найдено.");
        }
        CommentModel comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ID " + commentId + " не найден"));
        if (commentModel.getText() != null) {
            comment.setText(comment.getText());
        }
        commentRepository.save(comment);
    }

    //Метод для проверки, является ли пользователь владельцем комментария
    public boolean isOwner(Integer id, String username) {
        CommentModel comment = commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление не найдено"));
        return comment.getOwner().getUsername().equals(username);
    }

    public boolean existsById(Integer id) {
        return commentRepository.existsById(id);
    }
}
