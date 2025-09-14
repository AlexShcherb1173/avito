package ru.skypro.homework.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.security.SecurityUtil;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления комментариями к объявлениям.
 * Отвечает за получение, создание, редактирование и удаление комментариев.
 * Проверяет права доступа на уровне пользователя.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository comments;
    private final AdRepository ads;
    private final CommentMapper mapper;
    private final SecurityUtil sec;

    public CommentServiceImpl(CommentRepository comments, AdRepository ads, CommentMapper mapper, SecurityUtil sec) {
        this.comments = comments;
        this.ads = ads;
        this.mapper = mapper;
        this.sec = sec;
    }

    /**
     * Получает все комментарии, относящиеся к объявлению по его ID.
     *
     * @param adId ID объявления
     * @return DTO-объект {@link Comments} с количеством и списком комментариев
     */
    @Override
    @Transactional(readOnly = true)
    public Comments getComments(int adId) {
        List<Comment> list = comments.findAllByAd_Id(adId).stream().map(mapper::toDto).collect(Collectors.toList());
        Comments res = new Comments();
        res.setCount(list.size());
        res.setResults(list);
        return res;
    }

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * @param adId ID объявления
     * @param dto  DTO с текстом нового комментария
     * @return DTO добавленного комментария
     * @throws IllegalArgumentException если объявление не найдено
     */
    @Override
    public Comment addComment(int adId, CreateOrUpdateComment dto) {
        AdEntity ad = ads.findById(adId).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        UserEntity me = sec.currentUser();
        CommentEntity c = mapper.fromCreateDto(dto);
        c.setAd(ad);
        c.setAuthor(me);
        c.setCreatedAt(Instant.now());
        return mapper.toDto(comments.save(c));
    }

    /**
     * Удаляет комментарий по ID. Доступно автору комментария или администратору.
     *
     * @param adId       ID объявления
     * @param commentId  ID комментария
     * @throws AccessDeniedException если пользователь не имеет прав на удаление
     * @throws IllegalArgumentException если комментарий не найден
     */
    @Override
    public void deleteComment(int adId, int commentId) {
        CommentEntity c = comments.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, c.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        comments.delete(c);
    }

    /**
     * Обновляет текст комментария. Доступно только автору или администратору.
     *
     * @param adId       ID объявления
     * @param commentId  ID комментария
     * @param dto        DTO с новым текстом комментария
     * @return DTO обновлённого комментария
     * @throws AccessDeniedException если пользователь не имеет прав на обновление
     * @throws IllegalArgumentException если комментарий не найден
     */
    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment dto) {
        CommentEntity c = comments.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, c.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        c.setText(dto.getText());
        return mapper.toDto(c);
    }
}

