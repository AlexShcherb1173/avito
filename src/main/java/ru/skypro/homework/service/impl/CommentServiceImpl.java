package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.mapper.AppMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AppMapper mapper;

    @Override
    public List<CommentDTO> getCommentsForAd(Integer adId) {
        return commentRepository.findAllByAdId(adId)
                .stream()
                .map(mapper::commentEntityToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO addComment(Integer adId, CreateOrUpdateComment createComment) {
        Comment comment = mapper.createOrUpdateCommentToCommentEntity(createComment);
        comment.setAdId(adId); // Связываем комментарий с объявлением
        return mapper.commentEntityToCommentDTO(commentRepository.save(comment));
    }

    @Override
    public CommentDTO editComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment) {
        Comment existingComment = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));

        // Применяем изменения из DTO
        existingComment.setText(updateComment.getText());
        existingComment.setUpdatedAt(java.time.LocalDateTime.now());

        return mapper.commentEntityToCommentDTO(commentRepository.save(existingComment));
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteByIdAndAdId(commentId, adId);
    }
}