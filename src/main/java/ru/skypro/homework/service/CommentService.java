package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public Comments getComments(Integer adId) {
        List<Comment> results = commentRepository.findAllByAd_Id(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        Comments comments = new Comments();
        comments.setCount(results.size());
        comments.setResults(results);
        return comments;
    }

    public Comment addComment(Integer adId, Integer authorId, CreateOrUpdateComment createOrUpdateComment) {
        AdEntity ad = adRepository.findById(adId).orElseThrow();
        UserEntity author = userRepository.findById(authorId).orElseThrow();

        CommentEntity entity = new CommentEntity();
        entity.setAd(ad);
        entity.setAuthor(author);
        entity.setCreatedAt(Instant.now());

        commentMapper.applyCreateOrUpdate(createOrUpdateComment, entity);

        CommentEntity saved = commentRepository.save(entity);
        return commentMapper.toDto(saved);
    }

    public Comment updateComment(Integer commentId, CreateOrUpdateComment createOrUpdateComment) {
        return commentRepository.findById(commentId)
                .map(entity -> {
                    // ВАЖНО: порядок аргументов для MapStruct
                    commentMapper.applyCreateOrUpdate(createOrUpdateComment, entity);
                    return commentMapper.toDto(entity);
                })
                .orElse(null);
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}
