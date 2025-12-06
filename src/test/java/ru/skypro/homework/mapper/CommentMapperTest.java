package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    private CommentMapper commentMapper;

    private UserEntity testUser;
    private CommentEntity testComment;
    private CreateOrUpdateCommentDto testCreateDto;

    @BeforeEach
    void setUp() {
        commentMapper = new CommentMapper();

        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setFirstName("Иван");
        testUser.setImage("avatar.jpg");

        testComment = new CommentEntity();
        testComment.setId(1);
        testComment.setText("Test comment");
        testComment.setAuthor(testUser);
        testComment.setCreatedAt(LocalDateTime.of(2025, 12, 6, 11, 0, 0));

        testCreateDto = new CreateOrUpdateCommentDto();
        testCreateDto.setText("New comment");
    }

    @Test
    void toDto_WhenEntityNotNull_ShouldReturnCommentDto() {
        // When
        CommentDto result = commentMapper.toDto(testComment);

        // Then
        assertNotNull(result);
        assertEquals(testComment.getId(), result.getPk());
        assertEquals(testComment.getText(), result.getText());
        assertEquals(testUser.getId(), result.getAuthor());
        assertEquals(testUser.getFirstName(), result.getAuthorFirstName());
        assertEquals(testUser.getImage(), result.getAuthorImage());

        // Проверяем конвертацию времени
        long expectedMillis = testComment.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        assertEquals(expectedMillis, result.getCreateAt());
    }

    @Test
    void toDto_WhenEntityNull_ShouldReturnNull() {
        // When
        CommentDto result = commentMapper.toDto(null);

        // Then
        assertNull(result);
    }

    @Test
    void toDto_WhenAuthorNull_ShouldHandleGracefully() {
        // Given
        testComment.setAuthor(null);

        // When
        CommentDto result = commentMapper.toDto(testComment);

        // Then
        assertNotNull(result);
        assertNull(result.getAuthor());
        assertNull(result.getAuthorFirstName());
        assertNull(result.getAuthorImage());
    }

    @Test
    void toDto_WhenCreatedAtNull_ShouldSetNull() {
        // Given
        testComment.setCreatedAt(null);

        // When
        CommentDto result = commentMapper.toDto(testComment);

        // Then
        assertNotNull(result);
        assertNull(result.getCreateAt());
    }

    @Test
    void toEntity_ShouldReturnCommentEntity() {
        // When
        CommentEntity result = commentMapper.toEntity(testCreateDto);

        // Then
        assertNotNull(result);
        assertEquals(testCreateDto.getText(), result.getText());
        assertNotNull(result.getCreatedAt()); // Должно быть установлено текущее время
        assertNull(result.getAuthor()); // Автор не устанавливается в этом методе
        assertNull(result.getAd()); // Объявление не устанавливается в этом методе
    }

    @Test
    void toEntity_WhenDtoNull_ShouldReturnNull() {
        // When
        CommentEntity result = commentMapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    void updateEntityFromDto_ShouldUpdateText() {
        // Given
        CommentEntity entity = new CommentEntity();
        entity.setText("Old text");

        CreateOrUpdateCommentDto updateDto = new CreateOrUpdateCommentDto();
        updateDto.setText("Updated text");

        // When
        commentMapper.updateEntityFromDto(entity, updateDto);

        // Then
        assertEquals("Updated text", entity.getText());
    }

    @Test
    void updateEntityFromDto_WhenDtoNull_ShouldNotUpdate() {
        // Given
        CommentEntity entity = new CommentEntity();
        entity.setText("Old text");

        // When
        commentMapper.updateEntityFromDto(entity, null);

        // Then
        assertEquals("Old text", entity.getText());
    }

    @Test
    void updateEntityFromDto_WhenEntityNull_ShouldNotThrow() {
        // Should not throw exception
        commentMapper.updateEntityFromDto(null, testCreateDto);
    }

    @Test
    void updateEntityFromDto_WhenTextNull_ShouldNotUpdate() {
        // Given
        CommentEntity entity = new CommentEntity();
        entity.setText("Old text");

        CreateOrUpdateCommentDto updateDto = new CreateOrUpdateCommentDto();
        // text is null

        // When
        commentMapper.updateEntityFromDto(entity, updateDto);

        // Then
        assertEquals("Old text", entity.getText());
    }
}
