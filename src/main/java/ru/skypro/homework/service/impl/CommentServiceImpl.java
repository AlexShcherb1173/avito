package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdRepository adRepository;
    private final UsersRepository usersRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsForAd(Integer adId) {
        return commentRepository.findAllByAdId(adId)
                .stream()
                .map(commentMapper::commentEntityToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO addComment(Integer adId, CreateOrUpdateComment createComment, String username) {
        Users author = usersRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        Comment comment = new Comment();
        comment.setText(createComment.getText());
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        return commentMapper.commentEntityToCommentDTO(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDTO editComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment, Authentication authentication) {
        String username = authentication.getName();
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        // Проверяем, является ли пользователь администратором
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !existingComment.getAuthor().getEmail().equals(username)) {
            throw new ForbiddenException("You can only edit your own comments");
        }

        existingComment.setText(updateComment.getText());
        return commentMapper.commentEntityToCommentDTO(commentRepository.save(existingComment));
    }

    @Override
    @Transactional
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) {
        String username = authentication.getName();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        // Проверяем, является ли пользователь администратором
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !comment.getAuthor().getEmail().equals(username)) {
            throw new ForbiddenException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }
}