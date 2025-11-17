package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Picture;
import ru.skypro.homework.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    Picture picture = Picture.builder().id(1).build();

    @Test
    void testCommentToCommentDto() {
        // Given
        User author = User.builder().id(1).firstName("John").image(picture).build();

        Comment comment = Comment.builder().id(100).text("This is a test comment").createdAt(1672531200000L).author(author).build();

        // When
        CommentDto commentDto = commentMapper.CommentToCommentDto(comment);

        // Then
        assertNotNull(commentDto);
        assertEquals(100, commentDto.getPk());
        assertEquals(1, commentDto.getAuthor());
        assertEquals("/pictures/1", commentDto.getAuthorImage());
        assertEquals("John", commentDto.getAuthorFirstName());
        assertEquals(1672531200000L, commentDto.getCreatedAt());
        assertEquals("This is a test comment", commentDto.getText());
    }

    @Test
    void testCreateOrUpdateCommentDtoToCommentEntity() {
        // Given
        CreateOrUpdateCommentDto createDto = CreateOrUpdateCommentDto.builder().text("New comment text").build();

        // When
        Comment comment = commentMapper.CreateOrUpdateCommentDtoToCommentEntity(createDto);

        // Then
        assertNotNull(comment);
        assertEquals("New comment text", comment.getText());
        assertNull(comment.getId());
        assertNull(comment.getCreatedAt());
        assertNull(comment.getAd());
        assertNull(comment.getAuthor());
    }

    @Test
    void testCommentListToCommentDtoList() {
        // Given
        User author = User.builder().id(1).firstName("John").image(picture).build();

        Comment comment1 = Comment.builder().id(1).text("First comment").createdAt(1672531200000L).author(author).build();

        Comment comment2 = Comment.builder().id(2).text("Second comment").createdAt(1672617600000L).author(author).build();

        List<Comment> comments = List.of(comment1, comment2);

        // When
        List<CommentDto> listOfCommentDto = commentMapper.CommentListToCommentDtoList(comments);

        // Then
        assertNotNull(listOfCommentDto);
        assertEquals(2, listOfCommentDto.size());

        CommentDto dto1 = listOfCommentDto.get(0);
        assertEquals(1, dto1.getPk());
        assertEquals("First comment", dto1.getText());
        assertEquals("John", dto1.getAuthorFirstName());

        CommentDto dto2 = listOfCommentDto.get(1);
        assertEquals(2, dto2.getPk());
        assertEquals("Second comment", dto2.getText());
        assertEquals("John", dto2.getAuthorFirstName());
    }

    @Test
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            commentMapper.CommentToCommentDto(null);
            commentMapper.CreateOrUpdateCommentDtoToCommentEntity(null);
            commentMapper.CommentListToCommentDtoList(null);
        });
    }
}