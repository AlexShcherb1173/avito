package ru.skypro.homework.service.impl;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.CommentsResponse;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
private final AdRepository adRepository;
private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        }

    @Override
    public CommentDto addComment(Long adId, Long userId, CreateOrUpdateComment dto) {
        // 1. Находим объявление
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));

        // 2. Находим автора комментария
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 3. Создаём комментарий
        Comment comment = new Comment();
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setText(dto.getText());
        comment.setCreatedAt(LocalDateTime.now());

        // 4. Сохраняем в БД
        Comment saved = commentRepository.save(comment);

        // 5. Конвертируем в DTO и возвращаем
        return convertToCommentDto(saved);
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
        dto.setAuthorImage("/images/users/" + comment.getAuthor().getImage()); // если есть
        dto.setAuthorFirstName(comment.getAuthor().getFirstName());
        dto.setCreatedAt(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toEpochSecond());
        dto.setText(comment.getText());
        return dto;
    }
}