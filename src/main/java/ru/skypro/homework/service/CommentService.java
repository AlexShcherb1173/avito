package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с комментариями.
 *
 * Содержит бизнес-логику получения, создания,
 * обновления и удаления комментариев к объявлениям.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /**
     * Получить список комментариев для объявления.
     *
     * @param adId идентификатор объявления
     * @return Optional с объектом Comments
     */
    public Optional<Comments> getCommentsByAdId(Integer adId) {
        if (!adRepository.existsById(adId)) {
            return Optional.empty();
        }

        List<Comment> commentDtos = commentRepository.findByAdId(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        Comments comments = new Comments();
        comments.setCount(commentDtos.size());
        comments.setResults(commentDtos);
        return Optional.of(comments);
    }

       /**
         * Добавить комментарий к объявлению.
         *
         * @param adId идентификатор объявления
         * @param dto данные нового комментария
         * @param email email текущего пользователя
         * @return Optional с созданным комментарием
         */
    public Optional<Comment> addComment(Integer adId, CreateOrUpdateComment dto, String email) {
        Optional<AdEntity> adOptional = adRepository.findById(adId);
        Optional<UserEntity> authorOptional = userRepository.findByEmail(email);

        if (adOptional.isEmpty() || authorOptional.isEmpty()) {
            return Optional.empty();
        }

        CommentEntity entity = commentMapper.fromDto(dto, authorOptional.get(), adOptional.get());
        CommentEntity savedComment = commentRepository.save(entity);
        return Optional.of(commentMapper.toDto(savedComment));
    }

    /**
         * Обновить комментарий.
         *
         * @param commentId идентификатор комментария
         * @param dto новые данные комментария
         * @param email email текущего пользователя
         * @return Optional с обновленным комментарием
         */
    public Optional<Comment> updateComment(Integer commentId, CreateOrUpdateComment dto, String email) {
        Optional<CommentEntity> commentOptional = commentRepository.findById(commentId);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (commentOptional.isEmpty() || userOptional.isEmpty()) {
            return Optional.empty();
        }

        CommentEntity entity = commentOptional.get();
        UserEntity currentUser = userOptional.get();

        if (!canEditComment(entity, currentUser)) {
            throw new AccessDeniedException("You cannot edit чужой комментарий");
        }

        commentMapper.updateCommentFields(dto, entity);
        CommentEntity updatedComment = commentRepository.save(entity);
        return Optional.of(commentMapper.toDto(updatedComment));
    }

        /**
         * Удалить комментарий.
         *
         * @param commentId идентификатор комментария
         * @param email email текущего пользователя
         * @return true, если комментарий удален
         */
    public boolean deleteComment(Integer commentId, String email) {
        Optional<CommentEntity> commentOptional = commentRepository.findById(commentId);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (commentOptional.isEmpty() || userOptional.isEmpty()) {
            return false;
        }

        CommentEntity entity = commentOptional.get();
        UserEntity currentUser = userOptional.get();

        if (!canEditComment(entity, currentUser)) {
            throw new AccessDeniedException("You cannot delete чужой комментарий");
        }

        commentRepository.delete(entity);
        return true;
    }

             /**
              * Проверить, может ли пользователь редактировать или удалять комментарий.
              *
              * @param commentEntity комментарий
              * @param currentUser текущий пользователь
              * @return true, если пользователь является автором комментария или ADMIN
              */
    private boolean canEditComment(CommentEntity commentEntity, UserEntity currentUser) {
        return commentEntity.getAuthor().getId().equals(currentUser.getId())
                || "ADMIN".equals(currentUser.getRole());
    }
}