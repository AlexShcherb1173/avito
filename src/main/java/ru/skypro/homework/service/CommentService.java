package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateCommentDto;
import ru.skypro.homework.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentListMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final CommentMapper commentMapper;
    private final AdsService adsService;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final CommentListMapper commentListMapper;

    /**
     * Получение списка комментариев объявления.
     *
     * @param id id объявления.
     * @return Список комментариев в формате ResponseWrapperCommentDto.
     */
    public ResponseWrapperCommentDto getCommentsByAdId(Integer id) {
        return commentListMapper.toResponseWrapperCommentDto(adsService.getAdById(id));
    }

    /**
     * Добавление комментария.
     *
     * @param id               id объявления.
     * @param createCommentDto текст комментария.
     * @param username         имя пользователя из аутентификации.
     * @return Созданный комментарий в формате CommentDto.
     */
    public CommentDto addComment(Integer id, CreateCommentDto createCommentDto, String username) {

        User author = userService.getUserByUsername(username);
        Ads ad = adsService.getAdById(id);

        Comment comment = commentMapper.toComment(createCommentDto);
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(System.currentTimeMillis());
        comment = commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    /**
     * Удаление комментария из базы данных.
     *
     * @param commentId id комментария.
     * @param username  имя пользователя из аутентификации.
     * @return true - если удаление прошло успешно, false - если удаление было запрещено из-за недостатка прав.
     */
    public boolean deleteComment(Integer commentId, String username) {

        User user = userService.getUserByUsername(username);
        Comment comment = getCommentById(commentId);

        if (user.equals(comment.getAuthor()) || user.getRole() == Role.ADMIN) {
            commentRepository.deleteById(commentId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Обновление комментария в базе данных.
     *
     * @param commentId  id комментария.
     * @param commentDto dto с данными для обновления.
     * @param username   имя пользователя из аутентификации.
     * @return Обновлённый комментарий в формате CommentDto, если операция прошла успешно,
     * или пустой Optional, если обновление было запрещено из-за недостатка прав.
     */
    public Optional<CommentDto> updateComment(Integer commentId, CommentDto commentDto, String username) {

        User user = userService.getUserByUsername(username);
        Comment comment = getCommentById(commentId);

        if (user.equals(comment.getAuthor()) || user.getRole() == Role.ADMIN) {
            commentMapper.updateComment(commentDto, comment);
            return Optional.of(commentMapper.toDto(commentRepository.save(comment)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Получение комментария из базы данных по id.
     */
    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment with id=" + id + " not found"));
    }


}