package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;
    private UserService userService;
    private AdRepository adRepository;


    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, UserService userService, AdRepository adRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.adRepository = adRepository;
    }

    @Override
    public CommentsDTO getComments(int adId) {
        List<Comment> comments = commentRepository.getCommentsByAdId(adId);

        List<CommentDTO> commentDTOS = comments.stream()
                .map(comment -> commentMapper.commentToCommentDTO(comment))
                .collect(Collectors.toList());

        return new CommentsDTO(commentDTOS.size(), commentDTOS);
    }

    @Override
    public CommentDTO addComment(int adId, String userName, CreateOrUpdateCommentDTO text) {
        Comment comment = new Comment();

        comment.setCreatedAt(Instant.now().toEpochMilli());
        comment.setText(text.getText());
        comment.setAd(adRepository.findById(adId).orElseThrow());
        comment.setAuthor(userService.getUser(userName));

        commentRepository.save(comment);

        return commentMapper.commentToCommentDTO(comment);
    }

    @Override
    public CommentDTO updateComment(int adId, int commentId, CreateOrUpdateCommentDTO text) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (comment.getAd().getPk() == adId) {
            comment.setText(text.getText());
            comment.setCreatedAt(Instant.now().toEpochMilli());
        }

        commentRepository.save(comment);

        return commentMapper.commentToCommentDTO(comment);
    }

    @Override
    public void deleteComment(int adId, int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (comment.getAd().getPk() != adId) {
            throw new AdNotFoundException();
        }

        commentRepository.deleteById(comment.getPk());
    }
}
