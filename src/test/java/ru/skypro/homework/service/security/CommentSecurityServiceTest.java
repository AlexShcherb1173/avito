package ru.skypro.homework.service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.CommentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentSecurityServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentSecurityService commentSecurityService;

    private final User currentUser = new User(1, "email@example.com", "pass", false,
            "Oleg", "Olegov", "+373777777", Role.USER, new Image(), null, null);
    private final Integer commentId = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(currentUser.getEmail());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testIsOwner_WhenCommentExistsAndIsOwner() {
        Comment comment = new Comment();
        comment.setAuthor(currentUser);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        boolean isOwner;
        if (commentSecurityService.isOwner(commentId)) isOwner = true;
        else isOwner = false;

        assertTrue(isOwner);
    }

    @Test
    void testIsOwner_WhenCommentExistsAndIsNotOwner() {
        User anotherUser = new User(2, "another_email@example.com", "pass", false,
                "Maxim", "Anisimov", "+373777777", Role.USER, new Image(), null, null);
        Comment comment = new Comment();
        comment.setAuthor(anotherUser);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        boolean isOwner = commentSecurityService.isOwner(commentId);

        assertFalse(isOwner);
    }

    @Test
    void testIsOwner_WhenCommentDoesNotExist() {
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentSecurityService.isOwner(commentId));
    }
}