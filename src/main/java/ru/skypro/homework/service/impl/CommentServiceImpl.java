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

    @Override
    @Transactional(readOnly = true)
    public Comments getComments(int adId) {
        List<Comment> list = comments.findAllByAd_Id(adId).stream().map(mapper::toDto).collect(Collectors.toList());
        Comments res = new Comments();
        res.setCount(list.size());
        res.setResults(list);
        return res;
    }

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

    @Override
    public void deleteComment(int adId, int commentId) {
        CommentEntity c = comments.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, c.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        comments.delete(c);
    }

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
