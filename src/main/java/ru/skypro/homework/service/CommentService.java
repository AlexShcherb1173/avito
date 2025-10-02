package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.dto.CommentDto;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentDto addComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAd(new Ad());
        comment.setAuthor(new User());
        commentRepository.save(comment);
        return new CommentDto(comment.getPk(), comment.getText(), comment.getAd().getPk(), comment.getAuthor().getId());
    }
}
