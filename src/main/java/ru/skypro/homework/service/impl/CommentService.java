package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdServiceApi;
import ru.skypro.homework.service.CommentServiceApi;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentService implements CommentServiceApi {

    private final CommentRepository commentRepository;

    private final AdServiceApi adService;

    @Override
    @Transactional
    public Comment addCommentToAd(Long adId, Comment newComment) {
        log.info("Добавление комментария: {}", newComment.getId() +
                ", " + newComment.getText() +
                ", " + newComment.getUser() +
                ", " + newComment.getCreatedAt() +
                " к объявлению " + adId
        );
        Comment save = commentRepository.save(newComment);
        Ad ad = adService.getAdById(adId);
        ad.getComments().add(save);
        adService.save(ad);
        return save;
    }

    @Override
    @Transactional
    public void deleteCommentFromAd(Long adId, Long commentId) {
        log.info("Удаление комментария с id: {}", commentId + ", объявления с id " + adId);
        Ad ad = adService.getAdById(adId);
        Comment comment = getCommentFromAdById(ad, commentId);
        ad.getComments().remove(comment);
        adService.save(ad);
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment updateComment(
            Long adId,
            Long commentId,
            CreateOrUpdateComment createOrUpdateComment
    ) {
        log.info("Редактирование комментария с id: {}", commentId + ", объявления с id " + adId);
        Ad ad = adService.getAdById(adId);
        Comment comment = getCommentFromAdById(ad, commentId);
        comment.setText(createOrUpdateComment.getText());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsForAd(Long id) {
        log.info("Получение всех комментариев объявления: {}", id);
        return adService.getAdById(id).getComments();
    }

    private Comment getCommentFromAdById(Ad ad, Long commentId) {
        log.info("Получение комментария с id: {}", commentId + ", объявления с id " + ad.getId());
        return ad.getComments().stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CommentNotFoundException(
                        "Комментарий с id=" + commentId + " не найден в объявлении с id=" + ad.getId()));
    }

    @Override
    public Comment getCommentById(Long id) {
        log.info("Получение комментария по id: {}", id);
        return commentRepository.findById(id).orElseThrow(
                () -> new CommentNotFoundException("Комментария с id=" + id + " не найдено")
        );
    }

}
