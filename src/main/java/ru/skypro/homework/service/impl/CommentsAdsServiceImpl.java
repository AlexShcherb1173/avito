package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.*;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repositories.AdvertRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.CommentsAdsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsAdsServiceImpl implements CommentsAdsService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentMapper mapper;

    @Override
    public CommentsDto getComments(Long id) {
        Advert advert = advertRepository.findById(id).orElseThrow(RuntimeException::new);
        List<CommentDto> commentsListDto = new ArrayList<>();
        for (Comment comment : commentRepository.findAllById(advert)) {
            commentsListDto.add(mapper.INSTANCE.toCommentDTO(comment, comment.getAuthor()));
        }

        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setResults(commentsListDto);
        commentsDto.setCount(commentsListDto.size());
        return commentsDto;
    }

    @Override
    public CommentDto createComment(Long id, CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        Advert advert = advertRepository.findById(id).orElseThrow(RuntimeException::new);

        Comment comment = new Comment();
        comment.setAdvert(advert);
        comment.setAuthor(user);
        comment.setText(createOrUpdateCommentDto.getText());
        comment.setCreatedAt(System.currentTimeMillis());

        return mapper.INSTANCE.toCommentDTO(commentRepository.save(comment), user);
    }

    @Override
    public Void deleteComment(Long id, Long commentId) {
        commentRepository.deleteById(commentId);
        return null;
    }

    @Override
    public CommentDto updateComment(Long id, Long commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Comment comment = commentRepository.findById(commentId).orElseThrow(RuntimeException::new);
        comment.setText(createOrUpdateCommentDto.getText());

        return mapper.INSTANCE.toCommentDTO(commentRepository.save(comment), user);
    }
}
