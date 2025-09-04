package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    public Comment addComment(long adId, long authorId, CreateOrUpdateComment req) {
        UserEntity author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + authorId));
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found: " + adId));
        CommentEntity entity = commentMapper.fromCreateRequest(req, author, ad);
        entity = commentRepository.save(entity);
        return commentMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(long adId) {
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found: " + adId));
        return commentRepository.findAllByAd(ad).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }
}
