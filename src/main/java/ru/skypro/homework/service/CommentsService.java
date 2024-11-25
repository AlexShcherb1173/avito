package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import java.nio.file.AccessDeniedException;

/**
 * Интерфейс CommentsService предоставляет методы для работы с комментариями.
 * Он включает операции для получения, добавления, обновления и удаления комментариев.
 */
public interface CommentsService {

    /**
     * <b>Получает список комментариев для указанного объявления. </b> <p>
     *
     * @param id идентификатор объявления, для которого нужно получить комментарии
     * @return объект CommentsDTO, содержащий список комментариев
     */
    CommentsDTO getComments(long id);

    /**
     * <b>Добавляет новый комментарий к указанному объявлению. </b> <p>
     *
     * @param id идентификатор объявления, к которому добавляется комментарий
     * @param createOrUpdateComment объект, содержащий данные нового комментария
     * @param authentication объект аутентификации пользователя
     * @return объект CommentDTO, представляющий добавленный комментарий
     */
    CommentDTO addComment(long id, CreateOrUpdateComment createOrUpdateComment, Authentication authentication);

    /**
     * <b>Удаляет комментарий по его идентификатору. </b> <p>
     *
     * @param listingId идентификатор объявления, к которому относится комментарий
     * @param commentId идентификатор удаляемого комментария
     * @param authentication объект аутентификации пользователя
     * @throws AccessDeniedException если пользователь не имеет прав на удаление комментария
     */
    void deleteComment(long listingId, long commentId, Authentication authentication) throws AccessDeniedException;

    /**
     * <b>Обновляет существующий комментарий. </b> <p>
     *
     * @param listingId идентификатор объявления, к которому относится комментарий
     * @param commentId идентификатор обновляемого комментария
     * @param createOrUpdateComment объект, содержащий обновленные данные комментария
     * @param authentication объект аутентификации пользователя
     * @return объект CommentDTO, представляющий обновленный комментарий
     * @throws AccessDeniedException если пользователь не имеет прав на обновление комментария
     */
    CommentDTO updateComment(long listingId, long commentId, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) throws AccessDeniedException;

}
