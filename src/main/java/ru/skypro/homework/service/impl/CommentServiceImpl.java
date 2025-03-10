package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.dto.RoleDTO;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link CommentService}, предоставляющая операции, связанные с комментариями.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserServiceImpl userService;

    /**
     * Преобразует сущность Comment в DTO.
     *
     * @param comment сущность комментария
     * @return объект CommentDTO
     */
    @Override
    public CommentDTO getCommentDTO(Comment comment) {
        return commentMapper.commentToCommentDTO(comment);
    }

    /**
     * Преобразует DTO комментария в сущность Comment.
     *
     * @param commentDTO DTO комментария
     * @return сущность Comment
     */
    @Override
    public Comment createCommentFromDTO(CommentDTO commentDTO) {
        return commentMapper.commentDTOToComment(commentDTO);
    }

    /**
     * Добавляет комментарий к объявлению.
     *
     * @param adId       идентификатор объявления
     * @param commentDTO DTO создаваемого комментария
     * @return объект CommentDTO созданного комментария
     */
    @Override
    public CommentDTO addComment(Integer adId, CreateOrUpdateCommentDTO commentDTO) {
        User author = userService.getCurrentUserEntity();
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));
        Comment comment = new Comment().setText(commentDTO.getText()).setAd(ad).setAuthor(author).setCreatedAt(Instant.now().getEpochSecond());
        commentRepository.save(comment);
        return commentMapper.commentToCommentDTO(comment);
    }

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param commentId идентификатор комментария
     * @throws RuntimeException если комментарий не найден или у пользователя нет прав на удаление
     */
    @Override
    public void deleteComment(Integer commentId) {
        User currentUser = userService.getCurrentUserEntity();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (currentUser.getRole() != RoleDTO.ADMIN && !comment.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new RuntimeException("You are not author of this comment");
        }
        commentRepository.delete(comment);
    }

    /**
     * Получает все комментарии к объявлению.
     *
     * @param adId идентификатор объявления
     * @return объект CommentsDTO с количеством и списком комментариев
     */
    @Override
    public CommentsDTO getComments(Integer adId) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));
        List<Comment> comments = commentRepository.findByAd(ad);
        List<CommentDTO> commentDTOs = comments.stream().map(commentMapper::commentToCommentDTO).collect(Collectors.toList());
        return new CommentsDTO(commentDTOs.size(), commentDTOs);
    }

    /**
     * Обновляет комментарий по его идентификатору.
     *
     * @param commentId  идентификатор комментария
     * @param commentDTO DTO с новыми данными
     * @return обновленный объект CommentDTO
     * @throws RuntimeException если комментарий не найден или у пользователя нет прав на изменение
     */
    @Override
    public CommentDTO updateComment(Integer commentId, CreateOrUpdateCommentDTO commentDTO) {
        User currentUser = userService.getCurrentUserEntity();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (currentUser.getRole() != RoleDTO.ADMIN && !comment.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new RuntimeException("You are not author of this comment");
        }
        comment.setText(commentDTO.getText());
        commentRepository.save(comment);
        return commentMapper.commentToCommentDTO(comment);
    }

    /**
     * Проверяет, является ли пользователь автором комментария.
     *
     * @param commentId идентификатор комментария
     * @param username  имя пользователя
     * @return true, если пользователь является автором комментария, иначе false
     */
    @Override
    public boolean isCommentAuthor(Integer commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        return comment.getAuthor().getUsername().equals(username);
    }
}
