package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.generatedDto.CommentDto;
import ru.skypro.homework.dto.generatedDto.CommentsDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final AdService adService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentServiceImpl(AdService adService, CommentMapper commentMapper, CommentRepository commentRepository, UserService userService) {
        this.adService = adService;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Override
    public CommentDto createNewComment(Long adId, CommentDto commentDto, Authentication authentication) {
        Ad adById = adService.getAdById(adId);
        User author = userService.getUser(authentication.getName());
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setAd(adById);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(savedComment);
    }

    @Override
    public CommentsDto getAllCommentsForAd(Long adsId) {
        Ad ad = adService.getAdById(adsId);
        List<Comment> comments = ad.getComments();
        return commentMapper.commentListToCommentsDto(comments.size(), comments);
    }

    @Override
    public void deleteComment(long adPk, long id, Authentication authentication) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        userService.checkUserHasPermit(authentication, comment.getAuthor().getUsername());
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(long adPk, long id, CommentDto commentDto, Authentication authentication) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        userService.checkUserHasPermit(authentication, comment.getAuthor().getUsername());
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }


}
