package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.dto.RoleDTO;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.utility.SecurityUtil;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserServiceImpl userService;
    private final SecurityUtil securityUtil;

    public CommentServiceImpl(CommentMapper commentMapper, CommentRepository commentRepository,
                              AdRepository adRepository, UserServiceImpl userService, SecurityUtil securityUtil) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @Override
    public CommentDTO getCommentDTO(Comment comment) {
        return commentMapper.commentToCommentDTO(comment);
    }

    @Override
    public Comment createCommentFromDTO(CommentDTO commentDTO) {
        return commentMapper.commentDTOToComment(commentDTO);
    }

    @Override
    public CommentDTO addComment(Integer adId, CreateOrUpdateCommentDTO commentDTO) {
        User author = userService.getCurrentUserEntity();

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        Comment comment = new Comment()
                .setText(commentDTO.getText())
                .setAd(ad)
                .setAuthor(author)
                .setCreatedAt(Instant.now().getEpochSecond());
        commentRepository.save(comment);
        return commentMapper.commentToCommentDTO(comment);
    }

    @Override
    public void deleteComment(Integer commentId) {
        User currentUser = userService.getCurrentUserEntity();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (currentUser.getRole() != RoleDTO.ADMIN && !comment.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new RuntimeException("You are not author of this comment");
        }
        commentRepository.delete(comment);
    }

    @Override
    public CommentsDTO getComments(Integer adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        List<Comment> comments = commentRepository.findByAd(ad);
        List<CommentDTO> commentDTOs = comments.stream()
                .map(commentMapper::commentToCommentDTO)
                .collect(Collectors.toList());
        return new CommentsDTO(commentDTOs.size(), commentDTOs);
    }

    @Override
    public CommentDTO updateComment(Integer commentId, CreateOrUpdateCommentDTO commentDTO) {
        User currentUser = userService.getCurrentUserEntity();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (currentUser.getRole() != RoleDTO.ADMIN && !comment.getAuthor().getUsername().equals(currentUser.getUsername())) {
            throw new RuntimeException("You are not author of this comment");
        }
        comment.setText(commentDTO.getText());
        commentRepository.save(comment);
        return commentMapper.commentToCommentDTO(comment);
    }
    @Override
    public boolean isCommentAuthor(Integer commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return comment.getAuthor().getUsername().equals(username);
    }
}
