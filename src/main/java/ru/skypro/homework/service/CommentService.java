package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean canEditComment(Integer commentId, Authentication authentication) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User user = userRepository.findByEmail(authentication.getName());
        return comment.getAuthor().getId().equals(user.getId()) || user.getRole().equals(User.Role.ADMIN);
    }

    public Comment addComment(Comment comment, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        comment.setAuthor(user);
        return commentRepository.save(comment);
    }
}
