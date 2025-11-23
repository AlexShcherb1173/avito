package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.CollectionMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CollectionMapper collectionMapper;

    @Override
    @Transactional(readOnly = true)
    public CommentsDto getComments(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new EntityNotFoundException("Ad not found with id: " + adId);
        }
        List<CommentEntity> comments = commentRepository.findByAdId(adId);
        return collectionMapper.toCommentsDto(comments);
    }

    @Override
    public CommentsDto createComment(Integer adId, CreateOrUpdateCommentDto createOrUpdateCommentDto, String username) {
        UserEntity author = getUserByUsername(username);
        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + adId));


        return null;
    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto, String username) {
        return null;
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId, String username) {

    }

    private UserEntity getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }
}
