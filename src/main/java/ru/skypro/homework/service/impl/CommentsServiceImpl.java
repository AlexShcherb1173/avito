package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Listing;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ListingRepository;
import ru.skypro.homework.service.CommentsService;

import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <b>Реализация сервиса для работы с комментариями. </b> <p>
 * Предоставляет методы для получения, добавления, обновления и удаления комментариев.
 */
@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final ListingRepository listingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    /**
     * <b>Получает список комментариев для указанного объявления. </b> <p>
     *
     * @param id идентификатор объявления
     * @return объект CommentsDTO, содержащий количество и список комментариев
     */
    @Override
    public CommentsDTO getComments(long id) {
        List<Comment> commentList = commentRepository.findCommentsByListingId(id);
        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setCount(commentList.size());
        commentsDTO.setResults(commentList.stream()
                .map(commentMapper ::commentToCommentDto)
                .collect(Collectors.toList()));
        return commentsDTO;
    }

    /**
     * <b>Добавляет новый комментарий к указанному объявлению. </b> <p>
     *
     * @param id идентификатор объявления
     * @param createOrUpdateComment данные комментария для создания
     * @param authentication объект аутентификации текущего пользователя
     * @return объект CommentDTO, представляющий добавленный комментарий
     * @throws NotFoundException если объявление с указанным ID не найдено
     */
    @Override
    public CommentDTO addComment(long id, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        Listing listing = listingRepository.findById(id).orElseThrow(()->
                new NotFoundException("Объявление с ID " + id + " не найдено"));
        Comment comment = new Comment();
        comment.setText(createOrUpdateComment.getText());
        comment.setListing(listing);
        comment.setCreatedAt(LocalDateTime.now());
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        comment.setAuthor(user);
        commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    /**
     * <b>Удаляет комментарий, если у текущего пользователя есть права на это. </b> <p>
     *
     * @param listingId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param authentication объект аутентификации текущего пользователя
     * @throws AccessDeniedException если у пользователя нет прав на удаление комментария
     * @throws NotFoundException если комментарий с указанным ID не найден
     */
    @Override
    @Transactional
    public void deleteComment(long listingId, long commentId, Authentication authentication) throws AccessDeniedException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с ID" + commentId + "не найден"));
        checkPermit(comment, authentication);
        commentRepository.delete(comment);
    }

    /**
     * <b>Обновляет существующий комментарий, если у текущего пользователя есть права на это. </b> <p>
     *
     * @param listingId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param createOrUpdateComment данные для обновления комментария
     * @param authentication объект аутентификации текущего пользователя
     * @return объект CommentDTO, представляющий обновленный комментарий
     * @throws AccessDeniedException если у пользователя нет прав на обновление комментария
     * @throws NotFoundException если комментарий с указанным ID не найден
     */
    @Override
    @Transactional
    public CommentDTO updateComment(long listingId, long commentId, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) throws AccessDeniedException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new NotFoundException("Комментарий с ID" + commentId + "не найден"));
        checkPermit(comment, authentication);
        comment.setText(createOrUpdateComment.getText());
        return commentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    /**
     * <b>Проверяет, имеет ли текущий пользователь право редактировать или удалять комментарий. </b> <p>
     *
     * @param comment объект комментария для проверки прав доступа
     * @param authentication объект аутентификации текущего пользователя
     * @throws AccessDeniedException если у пользователя нет прав на редактирование или удаление комментария
     */
    public void checkPermit(Comment comment, Authentication authentication) throws AccessDeniedException {
        if (!comment.getAuthor().getEmail().equals(authentication.getName()) && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new AccessDeniedException("Вы не можете редактировать или удалять чужое объявление");
        }
    }
}
