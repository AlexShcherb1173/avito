package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.UserRole;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.CommentMapperManual;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с комментариями к объявлениям.
 * <p>
 * Обеспечивает получение/создание/изменение/удаление комментариев, связывает комментарии с объявлениями и авторами,
 * выполняет проверку прав (автор комментария или ADMIN) и маппинг Entity - DTO.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    //private final CommentMapper commentMapper;
    private final CommentMapperManual commentMapper;  // ✅ ручной

    /**
     * Возвращает список комментариев для указанного объявления.
     *
     * @param adId id объявления
     * @return DTO {@link Comments} со списком комментариев
     * @throws org.springframework.web.server.ResponseStatusException если объявление не найдено
     */
    public Comments getComments(Integer adId) {
        ensureAdExists(adId);

        List<Comment> results = commentRepository.findAllByAd_Id(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        Comments comments = new Comments();
        comments.setCount(results.size());
        comments.setResults(results);
        return comments;
    }

    /**
     * Добавляет комментарий к объявлению от имени текущего пользователя.
     *
     * @param adId id объявления
     * @param authorEmail email автора (из аутентификации)
     * @param dto DTO с текстом комментария
     * @return DTO {@link Comment} созданного комментария
     * @throws org.springframework.web.server.ResponseStatusException если объявление/пользователь не найдены
     */
    public Comment addComment(Integer adId, String authorEmail, CreateOrUpdateComment dto) {
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));

        UserEntity author = getUserByEmailOrThrow(authorEmail);

        CommentEntity entity = new CommentEntity();
        entity.setAd(ad);
        entity.setAuthor(author);
        entity.setCreatedAt(Instant.now());

        commentMapper.applyCreateOrUpdate(entity, dto);


        CommentEntity saved = commentRepository.save(entity);
        return commentMapper.toDto(saved);
    }

    /**
     * Обновляет комментарий, если пользователь имеет право (автор комментария или ADMIN).
     *
     * @param adId id объявления
     * @param commentId id комментария
     * @param currentEmail email текущего пользователя
     * @param dto DTO с обновляемым текстом
     * @return DTO {@link Comment} после обновления
     * @throws org.springframework.web.server.ResponseStatusException если комментарий/объявление не найдены или нет прав
     */
    public Comment updateComment(Integer adId, Integer commentId, String currentEmail, CreateOrUpdateComment dto) {
        ensureAdExists(adId);

        UserEntity currentUser = getUserByEmailOrThrow(currentEmail);

        CommentEntity comment = commentRepository.findByIdAndAd_Id(commentId, adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found for this ad"));


        checkCommentPermission(currentUser, comment);

        commentMapper.applyCreateOrUpdate(comment, dto);


        return commentMapper.toDto(comment);
    }

    /**
     * Удаляет комментарий, если пользователь имеет право (автор комментария или ADMIN).
     *
     * @param adId id объявления
     * @param commentId id комментария
     * @param currentEmail email текущего пользователя
     * @throws org.springframework.web.server.ResponseStatusException если комментарий/объявление не найдены или нет прав
     */
    public void deleteComment(Integer adId, Integer commentId, String currentEmail) {
        ensureAdExists(adId);

        UserEntity currentUser = getUserByEmailOrThrow(currentEmail);

        CommentEntity comment = commentRepository.findByIdAndAd_Id(commentId, adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found for this ad"));


        checkCommentPermission(currentUser, comment);

        commentRepository.delete(comment);
    }


    private void ensureAdExists(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found");
        }
    }

    private UserEntity getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private void checkCommentPermission(UserEntity currentUser, CommentEntity comment) {
        if (currentUser.getRole() == UserRole.ADMIN) {
            return;
        }
        Integer ownerId = comment.getAuthor() != null ? comment.getAuthor().getId() : null;
        if (ownerId == null || !ownerId.equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission for this comment");
        }
    }
}
