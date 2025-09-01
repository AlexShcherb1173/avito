package ru.skypro.homework.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.CommentsResponse;
import ru.skypro.homework.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        }

    @Override
    public CommentDto addComment(Long adId, Long userId, CreateOrUpdateComment dto) {
        // TODO: реализовать
        return null;
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {
        // TODO: реализовать
    }

    @Override
    public CommentDto updateComment(Long adId, Long commentId, CreateOrUpdateComment dto) {
        // TODO: реализовать
        return null;
    }

    @Override
    public CommentsResponse getComments(Long adId) {
        List<Comment> comments = commentRepository.findByAdId(adId);
        List<CommentDto> dtos = comments.stream()
                .map(this::convertToCommentDto)
                .collect(Collectors.toList());
        return new CommentsResponse(dtos.size(), dtos);
    }

    private CommentDto convertToCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setPk(Math.toIntExact(comment.getId()));
        dto.setAuthor(Math.toIntExact(comment.getAuthor().getId()));
        dto.setAuthorFirstName(comment.getAuthor().getFirstName());
        dto.setAuthorImage(comment.getAuthor().getImage()); // например: "/images/users/1.jpg"
        dto.setText(comment.getText());
        // createdAt — в миллисекундах с 1970 года
        dto.setCreatedAt(comment.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        return dto;
    }
}