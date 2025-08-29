package ru.skypro.homework.service.impl;


import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class CommentServiceImpl implements CommentService {
    public final CommentMapper commentMapper;
    public final CommentRepository commentRepository;
    public final UserRepository userRepository;
    public final UserServiceImpl userService;
    public final AdRepository adRepository;

    public CommentServiceImpl(CommentMapper commentMapper, CommentRepository commentRepository, UserRepository userRepository, UserServiceImpl userService, AdRepository adRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.adRepository = adRepository;
    }
@Override
    @Transactional(readOnly = true)
    public CommentsDto getComments(Integer id) {
        CommentsDto responseWrapperComment = new CommentsDto();
        List<CommentEntity> commentList = commentRepository.findAllByAdId(id);
        responseWrapperComment.setResults(commentMapper.commentListToCommentDtoList(commentList));
        responseWrapperComment.setCount(commentList.size());
        return responseWrapperComment;
    }
@Override

    public CommentDto addComment(Integer id, CreateOrUpdateCommentDto createCommentDto) {
        CommentEntity comment = commentMapper.toComment(createCommentDto);
        comment.setAd(adRepository.findById(id).orElse(null));
        comment.setUser(userRepository.findByUsername(userService.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found" + id)));
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);

    }
    @Override

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
    @Override

    public CommentDto updateComment(Integer commentId, CommentDto commentDto) {
        CommentEntity updatedComment = commentRepository.findById(commentId).orElseThrow();
        updatedComment.setText(commentDto.getText());
        commentRepository.save(updatedComment);
        return commentMapper.toCommentDto(updatedComment);
    }
    @Override
    @Transactional
    public void deleteCommentsByAdId(Integer adId) {
        commentRepository.deleteCommentsByAdId(adId);
    }

    @Override
    //@PreAuthorize("hasRole(‘ROLE_ADMIN’)")
    public boolean hasCommentAccess(Integer CommentId) {
        CommentEntity comment = commentRepository.findById(CommentId).orElseThrow();
        String currentUserRole = userService.getCurrentUserRole();
        String commentCreatorUsername = comment.getUser().getUsername();
        String currentUsername = userService.getCurrentUsername();
        return currentUserRole.equals("ADMIN") || commentCreatorUsername.equals(currentUsername);

    }
}
