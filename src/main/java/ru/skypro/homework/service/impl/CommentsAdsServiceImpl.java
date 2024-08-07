package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.service.CommentsAdsService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentsAdsServiceImpl implements CommentsAdsService {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private CommentMapper mapper;

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Comment comment = mapper.commentDtoToComment(commentDto);
        comment.setCreatedAt(LocalDateTime.now());;
        return mapper.commentToCommentDto(repository.save(comment));
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Optional<Comment> comment = repository.findById(id);
        return comment.map(mapper::commentToCommentDto).orElse(null);
    }
}
