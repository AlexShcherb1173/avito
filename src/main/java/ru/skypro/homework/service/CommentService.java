package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public Optional<Comments> getCommentsByAdId(Integer adId) {
        if (!adRepository.existsById(adId)) {
            return Optional.empty();
        }

        List<Comment> commentDtos = commentRepository.findByAdId(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        Comments comments = new Comments();
        comments.setCount(commentDtos.size());
        comments.setResults(commentDtos);
        return Optional.of(comments);
    }

    public Optional<Comment> addComment(Integer adId, CreateOrUpdateComment dto) {
        Optional<AdEntity> adOptional = adRepository.findById(adId);
        Optional<UserEntity> authorOptional = userRepository.findById(1);

        if (adOptional.isEmpty() || authorOptional.isEmpty()) {
            return Optional.empty();
        }

        CommentEntity entity = commentMapper.fromDto(dto, authorOptional.get(), adOptional.get());
        CommentEntity savedComment = commentRepository.save(entity);
        return Optional.of(commentMapper.toDto(savedComment));
    }

    public Optional<Comment> updateComment(Integer commentId, CreateOrUpdateComment dto) {
        Optional<CommentEntity> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            return Optional.empty();
        }

        CommentEntity entity = commentOptional.get();
        commentMapper.updateCommentFields(dto, entity);

        CommentEntity updatedComment = commentRepository.save(entity);
        return Optional.of(commentMapper.toDto(updatedComment));
    }

    public boolean deleteComment(Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            return false;
        }

        commentRepository.deleteById(commentId);
        return true;
    }
}