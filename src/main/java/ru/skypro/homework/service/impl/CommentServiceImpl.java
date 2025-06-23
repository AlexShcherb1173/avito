package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.Exception.AdNotFoundException;
import ru.skypro.homework.Exception.CommentNotFoundException;
import ru.skypro.homework.Exception.UserNotFoundException;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto) {

        User user = userRepository.findById(commentDto.getAuthor())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Ad ad = adRepository.findById(commentDto.getPk())
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setUser(user);
        comment.setAd(ad);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isCommentOwner(#id, authentication.name)")
    @Override
    @Transactional
    public CommentDto updateComment(Integer id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        comment.setText(commentDto.getText());

        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isCommentOwner(#id, authentication.name)")
    @Override
    @Transactional
    public void deleteComment(Integer id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment not found");
        }
        commentRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Comments getAllComments(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new AdNotFoundException("Ad not found"));
        Comments commentsDto = new Comments();
        commentsDto.setResults(ad.getComments().stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList()));
        commentsDto.setCount(ad.getComments().size());
        return commentsDto;
    }


}
