package ru.avito.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.avito.dto.comment.CommentDto;
import ru.avito.entity.Ad;
import ru.avito.entity.Comment;
import ru.avito.entity.Role;
import ru.avito.entity.User;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    private CommentMapper commentMapper;
    private Comment comment;
    private Instant createdAt;

    @BeforeEach
    void setUp() {
        commentMapper = new CommentMapper();

        User author = User.builder()
                .id(1)
                .email("user@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .image("/images/users/1/avatar.jpg")
                .build();

        Ad ad = Ad.builder()
                .id(10)
                .title("Test ad")
                .price(10000)
                .description("Test ad description")
                .author(author)
                .build();

        createdAt = Instant.now();

        comment = Comment.builder()
                .id(20)
                .text("Test comment")
                .createdAt(createdAt)
                .author(author)
                .ad(ad)
                .build();
    }

    @Test
    void toDtoShouldMapAllFieldsCorrectly() {
        CommentDto dto = commentMapper.toDto(comment);

        assertNotNull(dto);
        assertEquals(1, dto.getAuthor());
        assertEquals("/images/users/1/avatar.jpg", dto.getAuthorImage());
        assertEquals("Ivan", dto.getAuthorFirstName());
        assertEquals(createdAt.toEpochMilli(), dto.getCreatedAt());
        assertEquals(20, dto.getPk());
        assertEquals("Test comment", dto.getText());
    }
}