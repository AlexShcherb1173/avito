package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    private Comment comment;
    private CommentDTO commentDTO;
    private CreateOrUpdateCommentDTO createOrUpdateCommentDTO;
    private User author;
    private Ad ad;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1);
        author.setFirstName("John");

        Image userImage = new Image();
        userImage.setFilePath("avatar.jpg");
        author.setImage(userImage);

        ad = new Ad();
        ad.setId(10);

        comment = new Comment();
        comment.setId(2);
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setText("Test comment");

        commentDTO = new CommentDTO();
        commentDTO.setPk(10);
        commentDTO.setAuthor(1);
        commentDTO.setAuthorFirstName("John");
        commentDTO.setAuthorImage("avatar.jpg");
        commentDTO.setText("Test comment");

        createOrUpdateCommentDTO = new CreateOrUpdateCommentDTO();
        createOrUpdateCommentDTO.setText("New comment");
    }

    @Test
    void testToModelFromCommentDTO() {
        Comment mappedComment = commentMapper.toModel(commentDTO);
        assertNotNull(mappedComment);
        assertEquals(commentDTO.getPk(), mappedComment.getAd().getId());
        assertEquals(commentDTO.getAuthor(), mappedComment.getAuthor().getId());
        assertEquals(commentDTO.getText(), mappedComment.getText());
    }

    @Test
    void testToModelFromCreateOrUpdateCommentDTO() {
        Comment mappedComment = commentMapper.toModel(createOrUpdateCommentDTO);
        assertNotNull(mappedComment);
        assertEquals(createOrUpdateCommentDTO.getText(), mappedComment.getText());
    }

    @Test
    void testToDtoCommentDTO() {
        CommentDTO mappedDto = new CommentDTO();
        mappedDto.setPk(comment.getAd().getId());
        mappedDto.setAuthor(comment.getAuthor().getId());
        mappedDto.setAuthorFirstName(comment.getAuthor().getFirstName());
        mappedDto.setText(comment.getText());

        assertNotNull(mappedDto);
        assertEquals(comment.getAd().getId(), mappedDto.getPk());
        assertEquals(comment.getAuthor().getId(), mappedDto.getAuthor());
        assertEquals(comment.getAuthor().getFirstName(), mappedDto.getAuthorFirstName());
        assertEquals(comment.getText(), mappedDto.getText());
    }

    @Test
    void testToDtoCreateOrUpdateCommentDTO() {
        CreateOrUpdateCommentDTO mappedDto = commentMapper.toDtoCreateOrUpdateCommentDTO(comment);
        assertNotNull(mappedDto);
        assertEquals(comment.getText(), mappedDto.getText());
    }

    @Test
    void testToDtoCommentsDTO() {
        List<Comment> commentsList = Collections.singletonList(comment);

        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setCount(commentsList.size());

        List<CommentDTO> dtoList = commentsList.stream()
                .map(c -> {
                    CommentDTO dto = new CommentDTO();
                    dto.setPk(c.getAd().getId());
                    dto.setAuthor(c.getAuthor().getId());
                    dto.setAuthorFirstName(c.getAuthor().getFirstName());
                    dto.setText(c.getText());
                    return dto;
                })
                .collect(Collectors.toList());

        commentsDTO.setResults(dtoList);
        assertNotNull(commentsDTO);
        assertEquals(commentsList.size(), commentsDTO.getCount());
        assertEquals(commentsList.size(), commentsDTO.getResults().size());
    }
}