package ru.skypro.homework.service.impl;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;


    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, AdRepository adRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;

    }

    @Override
    public Comments getComments(Integer adId) {
        List<CommentEntity> entities = commentRepository.findByAdId(adId);
        Comments comments = new Comments();
        comments.setCount(entities.size());
        comments.setResults(entities.stream().map(CommentMapper.INSTANCE::toDto).collect(Collectors.toList()));
        return comments;
    }


    @Override
    @Transactional
    public Comment addComment(Integer adId, CreateOrUpdateComment comment, Authentication authentication) {
        Users author = userRepository.findByEmail(authentication.name()).orElseThrow(() -> new RuntimeException("User not found"));

        AdEntity ad = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));

        CommentEntity entity = CommentMapper.INSTANCE.toEntity(comment);
        entity.setAuthor(author);
        entity.setAd(ad);
        entity.setCreatedAt(LocalDateTime.now());

        CommentEntity savedEntity = commentRepository.save(entity);
        return CommentMapper.INSTANCE.toDto(savedEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentOwner(#commentId, authentication.name)")
    @Transactional
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentOwner(#commentId, authentication.name)")
    @Transactional
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment) {
        CommentEntity entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        entity.setText(comment.getText());
        CommentEntity savedEntity = commentRepository.save(entity);
        return CommentMapper.INSTANCE.toDto(savedEntity);
    }

}
