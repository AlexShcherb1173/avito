package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public List<CommentDto> getCommentsByAdId(Integer adId) {
        return commentRepository.findAllByAdId(adId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    public CommentDto addComment(Authentication authentication, Integer adId, CreateOrUpdateCommentDto dto) {
        User author = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        Comment comment = CommentMapper.toEntity(dto, author, ad);
        commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }

    public void deleteComment(Authentication authentication, Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        if (!comment.getAuthor().getEmail().equals(authentication.getName()) &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Нет прав для удаления комментария");
        }

        commentRepository.delete(comment);
    }

    public CommentDto updateComment(Authentication authentication, Integer commentId, CreateOrUpdateCommentDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        if (!comment.getAuthor().getEmail().equals(authentication.getName()) &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Нет прав для редактирования комментария");
        }

        comment.setText(dto.getText());
        commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }
}