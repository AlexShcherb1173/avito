package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserOrAdminService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final UserOrAdminService userOrAdminService;

    /**
     * @param id id объявления
     * @param text текст комментария
     * @return сохраненный комментарий
     * Добавляет комментарий к объявлению
     */
    @Override
    public Comment addCommentToAds(int id, CreateOrUpdateComment text, Authentication authentication) {
        log.info("Был вызван метод для добавления комментария к объявлению{},{},{}",id,text,authentication);
        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName()).orElseThrow();
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAds(adsRepository.findById(id).orElseThrow());
        comment.setText(text.getText());
        return commentRepository.save(comment);
    }

    /**
     * @param adId      id объявления
     * @param commentId id комментария
     *                  Удаляет комментарий
     * @return
     */
    @Override
    public boolean deleteComment(int adId, int commentId, Authentication authentication) {
        log.info("Был вызван метод для удаления комментария к объявлению{},{},{}",adId,commentId,authentication);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        User commentOwner = comment.getUser();
        if (userOrAdminService.isUserOrAdmin(authentication.getName(), commentOwner)) {
            if (comment.getAds().getId() != adId) {
                try {
                    throw new AdsNotFoundException();
                } catch (AdsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            commentRepository.delete(comment);
            return true;
        }
        return false;

    }

    /**
     * @param adId id объявления
     * @param commentId id комментария
     * @param text новый текст
     * @param authentication авторизация
     * @return
     */
    @Override
    public Comment update(int adId, int commentId, CreateOrUpdateComment text, Authentication authentication) {
        log.info("Был вызван метод для обновления комментария к объявлению{},{},{}",adId,commentId,authentication);
        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName()).orElseThrow();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        User commentOwner = comment.getUser();
        if (userOrAdminService.isUserOrAdmin(authentication.getName(), commentOwner)) {
            if (comment.getAds().getId() != adId) {
                try {
                    throw new AdsNotFoundException();
                } catch (AdsNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            comment.setText(text.getText());
            commentRepository.save(comment);
            adsRepository.save(comment.getAds());
            return comment;
        }


        return commentRepository.save(comment);

    }

    /**
     * @param id id объявления
     * @return список комментариев
     * Возвращает все комментарии по id объявления
     */
    @Override
    public List<Comment> getComments(int id) {
        log.info("Был вызван метод для получения всех комментариев объявления{}",id);
        return commentRepository.findAllByAdsId(id);
    }
}