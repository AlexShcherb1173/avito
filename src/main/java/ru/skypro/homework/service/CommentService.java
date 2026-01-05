package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.DtoMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    public Optional<Comments> getCommentsByAdId(Integer adId) {
        if (!adRepository.existsById(adId)) {
            return Optional.empty();
        }

        List<CommentEntity> commentEntities = commentRepository.findByAdId(adId);
        List<Comment> commentDtos = dtoMapper.toCommentList(commentEntities);

        Comments comments = new Comments();
        comments.setCount(commentDtos.size());
        comments.setResults(commentDtos);

        return Optional.of(comments);
    }

    public Optional<Comment> addComment(Integer adId, CreateOrUpdateComment commentDto, String authorEmail) {
        Optional<AdEntity> adOpt = adRepository.findById(adId);
        Optional<UserEntity> authorOpt = userRepository.findByEmail(authorEmail);

        if (adOpt.isPresent() && authorOpt.isPresent()) {
            CommentEntity commentEntity = dtoMapper.toCommentEntity(
                    commentDto,
                    authorOpt.get(),
                    adOpt.get()
            );
            CommentEntity savedComment = commentRepository.save(commentEntity);
            return Optional.of(dtoMapper.toComment(savedComment));
        }

        return Optional.empty();
    }

    public Optional<Comment> updateComment(Integer adId, Integer commentId, CreateOrUpdateComment commentDto) {
        return commentRepository.findById(commentId)
                .filter(comment -> comment.getAd().getId().equals(adId))
                .map(comment -> {
                    comment.setText(commentDto.getText());
                    CommentEntity updatedComment = commentRepository.save(comment);
                    return dtoMapper.toComment(updatedComment);
                });
    }

    public boolean deleteComment(Integer adId, Integer commentId) {
        return commentRepository.findById(commentId)
                .filter(comment -> comment.getAd().getId().equals(adId))
                .map(comment -> {
                    commentRepository.delete(comment);
                    return true;
                })
                .orElse(false);
    }
}
