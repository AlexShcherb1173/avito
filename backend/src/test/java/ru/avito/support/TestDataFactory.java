package ru.avito.support;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.avito.entity.Ad;
import ru.avito.entity.Comment;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.repository.AdRepository;
import ru.avito.repository.CommentRepository;
import ru.avito.repository.UserRepository;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TestDataFactory {

    public static final String DEFAULT_PASSWORD = "password123";
    public static final String ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser() {
        return createUser(
                "user@example.com",
                DEFAULT_PASSWORD,
                "Ivan",
                "Ivanov",
                "+79990000001",
                Role.USER
        );
    }

    public User createSecondUser() {
        return createUser(
                "second@example.com",
                DEFAULT_PASSWORD,
                "Petr",
                "Petrov",
                "+79990000002",
                Role.USER
        );
    }

    public User createAdmin() {
        return createUser(
                "admin@example.com",
                ADMIN_PASSWORD,
                "Admin",
                "User",
                "+79990000003",
                Role.ADMIN
        );
    }

    public User createUser(
            String email,
            String rawPassword,
            String firstName,
            String lastName,
            String phone,
            Role role
    ) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .role(role)
                .image(null)
                .build();

        return userRepository.save(user);
    }

    public Ad createAd(User author) {
        return createAd(
                author,
                "Test title",
                10_000,
                "Test ad description"
        );
    }

    public Ad createSecondAd(User author) {
        return createAd(
                author,
                "Second ad title",
                20_000,
                "Second ad description"
        );
    }

    public Ad createAd(User author, String title, Integer price, String description) {
        Ad ad = Ad.builder()
                .author(author)
                .title(title)
                .price(price)
                .description(description)
                .image(null)
                .build();

        return adRepository.save(ad);
    }

    public Comment createComment(User author, Ad ad) {
        return createComment(author, ad, "Test comment text");
    }

    public Comment createSecondComment(User author, Ad ad) {
        return createComment(author, ad, "Second comment text");
    }

    public Comment createComment(User author, Ad ad, String text) {
        Comment comment = Comment.builder()
                .author(author)
                .ad(ad)
                .text(text)
                .createdAt(Instant.now())
                .build();

        return commentRepository.save(comment);
    }
}