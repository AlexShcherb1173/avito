package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public Comments getComments(Integer adId) {
        List<CommentEntity> entities = commentRepository.findByAdId(adId);
        Comments comments = new Comments();
        comments.setCount(entities.size());
        comments.setResults(entities.stream().map(commentMapper::toDto).collect(Collectors.toList()));
        return comments;
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment comment) {
        CommentEntity entity = commentMapper.toEntity(comment);
        // TODO: установить автора и объявление
        CommentEntity savedEntity = commentRepository.save(entity);
        return commentMapper.toDto(savedEntity);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment) {
        CommentEntity entity = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        entity.setText(comment.getText());
        CommentEntity savedEntity = commentRepository.save(entity);
        return commentMapper.toDto(savedEntity);
    }

}
