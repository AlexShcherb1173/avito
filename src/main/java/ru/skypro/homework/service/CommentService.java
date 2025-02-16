package ru.skypro.homework.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Advertisement;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final List<Comment> comments = new ArrayList<>();
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final AdRepository adRepository;


    public Comments getComments(int adId) {
        List<Comment> commentList = commentRepository.findByAdvertisement_Id(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        return new Comments(commentList.size(), commentList);
    }

    public Comment addComment(Long adId, CreateOrUpdateComment createComment, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Advertisement advertisement = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        CommentEntity commentEntity = commentMapper.toEntity(createComment, user, advertisement);
        return commentMapper.toDto(commentRepository.save(commentEntity));
    }

    public void deleteComment(int adId, Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment updateComment(long adId, long commentId, CreateOrUpdateComment commentDto) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setText(commentDto.getText());
                    return commentMapper.toDto(commentRepository.save(comment));
                })
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));
    }

}

