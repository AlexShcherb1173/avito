package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.models.CommentEntity;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.CommentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    @Override
    public CommentsDto commentsAd(int id) {

        return commentMapper.adsDto(commentRepository.findCommentByAdId(id));
    }

    @Override
    @Transactional
    public CommentDto createComment(int id, CreateOrUpdateCommentDto dto, UserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такого объявления нет"));
        CommentEntity comment = commentMapper.toCommentEntity(dto, ad, user);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @adRepository.findById(#id)" +
            ".get().author.email == authentication.name")
    public void deleteComment(int id, int commentId) {
        CommentEntity entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментария нет"));
        if (entity.getAd() == null || entity.getAd().getId() != id) {
            throw new EntityNotFoundException("Такого объявления нет");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @adRepository.findById(#id)" +
            ".get().author.email == authentication.name")
    public CommentDto updateComment(int id, int commentId, CreateOrUpdateCommentDto dto) {
        CommentEntity entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментария нет"));
        if (entity.getAd() == null || entity.getAd().getId() != id) {
            throw new EntityNotFoundException("Такого объявления нет");
        }
        entity.setText(dto.getText());
        entity.setCreatedAd(LocalDateTime.now());
        entity = commentRepository.save(entity);
        return commentMapper.toCommentDto(entity);
    }
}
