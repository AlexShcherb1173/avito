package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.map.CommentMap;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private CommentMap commentMap;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    /**
     * Получение комментариев объявления
     *
     * @param adId = id объявления
     * @return = лист с комментариями
     */
    @Override

    public CommentsDto getComments(int adId) {
        return commentMap.toComments(commentRepository.findAllByAdId(adId));
    }

    /**
     * Добавление комментария к объявлению
     *
     * @param adId    = id объявления
     * @param comment = текст комментария
     * @return = CommentDto
     */
    @Override
    public CommentDto postComment(int adId, CreateOrUpdateCommentDto comment, String userName) {
       return commentMap.toDto(commentRepository.save(commentMap.dtoToEntity(comment, adId, getCurrentUser(userName).getId())));
    }

    /**
     * Удалить комментарий
     *
     * @param id = id комментария
     */
    @Override
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }

    /**
     * Обновление комментария
     *
     * @param commentId = id комментария
     * @return = CommentDto
     */
    @Override
    public CommentDto patchComment(int adId, int commentId, CreateOrUpdateCommentDto commentDto, String userName) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new EntityNotFoundException("Комментарий не найден"));
        comment.setText(commentDto.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(getCurrentUser(userName));
        return commentMap.toDto(commentRepository.save(comment));
    }
    private User getCurrentUser(String userName){
        return userRepository.findByUsername(userName).
                orElseThrow(() -> new UserNotFoundException("User not found."));
    }
}
