package ru.skypro.homework.service.impl;

import ru.skypro.homework.enity.User;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.security.SecurityUtils;
import ru.skypro.homework.enity.Ad;
import ru.skypro.homework.enity.Comment;
import ru.skypro.homework.enity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public Comments getForAd(Integer adId) {
        List<CommentDto> list = commentRepository.findByAdId(adId.longValue()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        Comments w = new Comments();
        w.setCount(list.size());
        w.setResults(list);
        return w;
    }

    @Override
    public CommentDto add(Integer adId, CreateOrUpdateComment dto) {
        User me = securityUtils.getCurrentUser();
        Ad ad = adRepository.findById(adId.longValue())
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));

        Comment c = new Comment();
        c.setText(dto.getText());
        c.setAd(ad);
        c.setAuthor(me);
        c.setCreatedAt(LocalDateTime.now());

        return toDto(commentRepository.save(c));
    }

    @Override
    public void delete(Integer adId, Integer commentId) {
        Comment c = commentRepository.findById(commentId.longValue())
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        checkOwnerOrAdmin(c.getAuthor());
        commentRepository.delete(c);
    }

    @Override
    public CommentDto update(Integer adId, Integer commentId, CreateOrUpdateComment dto) {
        Comment c = commentRepository.findById(commentId.longValue())
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        checkOwnerOrAdmin(c.getAuthor());
        c.setText(dto.getText());

        return toDto(commentRepository.save(c));
    }

    // ===== Маппинг =====
    private CommentDto toDto(Comment c) {
        CommentDto dto = new CommentDto();
        dto.setPk(c.getId().intValue());
        dto.setAuthor(c.getAuthor().getId().intValue());
        dto.setAuthorFirstName(c.getAuthor().getFirstName());
        dto.setAuthorImage(c.getAuthor().getImageUrl() != null ? "/users/" + c.getAuthor().getId() + "/image" : null);
        dto.setCreatedAt(c.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        dto.setText(c.getText());
        return dto;
    }

    // ===== Проверка прав =====
    private void checkOwnerOrAdmin(User owner) {
        User current = securityUtils.getCurrentUser();
        if (!Objects.equals(current.getId(), owner.getId()) &&
                !current.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Forbidden");
        }
    }
}