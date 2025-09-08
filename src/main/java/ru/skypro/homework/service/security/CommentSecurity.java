package ru.skypro.homework.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.skypro.homework.repository.CommentRepository;

@Component("commentSecurity")
@RequiredArgsConstructor
public class CommentSecurity {

    private final CommentRepository commentRepository;

    public boolean isOwner(Long commentId, Authentication authentication) {
        String username = authentication.getName();
        return commentRepository.findById(commentId)
                .map(c -> c.getAuthor() != null
                        && c.getAuthor().getUsername().equals(username))
                .orElse(false);
    }
}
