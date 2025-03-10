package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, AdRepository adRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
    }

    private String objectAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    public boolean verificationAuthorComment(Integer id) {
        String authentication = objectAuthentication();
        log.info("verificationAuthorComment {}", authentication);
        User user = (userRepository.findByEmail(authentication)).orElseThrow();
        Comment comment = (commentRepository.findById(id)).orElseThrow();
        return comment.getUser().getId().equals(user.getId());
    }

    @Override
    public CommentsDto getCommentsForAd(Integer adPk) {
        List<Comment> comments = commentRepository.findByAdPk(adPk);
        List<CommentDto> commentDtos = comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
        return new CommentsDto(commentDtos.size(), commentDtos);
    }

    @Override
    public CommentDto addComment(Integer adPk, CreateOrUpdateCommentDto commentDto) {
        String authentication = objectAuthentication();
        User user = (userRepository.findByEmail(authentication)).orElseThrow();
        Ad ad = (adRepository.findById(adPk)).orElseThrow();
        log.info("-- addComment {}", commentDto);
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(Instant.now().toEpochMilli());
        comment.setUser(user);
        comment.setAd(ad);
        // Здесь нужно добавить логику для установки объявления (Ad) по adId
        // Например, через AdRepository
        // comment.setAd(adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found")));
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public CommentDto updateComment(Integer adPk, Integer commentId, CreateOrUpdateCommentDto commentDto) {
        String authentication = objectAuthentication();
        User user = (userRepository.findByEmail(authentication)).orElseThrow();
        log.info("--- updateComment {}", user.getFirstName());
        Comment comment = (commentRepository.findById(commentId)).orElseThrow();

        if (comment.getUser().getId().equals(user.getId())) {
            comment.setText(commentDto.getText());
            Comment updatedComment = commentRepository.save(comment);
            return commentMapper.toCommentDto(updatedComment);
        } else {
            throw new RuntimeException("User is not authorized to update this comment");
        }

    }

    @Override
    public void deleteComment(Integer adPk, Integer commentId) {
        String authentication = objectAuthentication();
        User user = (userRepository.findByEmail(authentication)).orElseThrow();
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getUser().getId().equals(user.getId())) {
                commentRepository.delete(comment);
            } else {
                throw new RuntimeException("User is not authorized to delete this comment");
            }
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

}