package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.generatedDto.CommentDto;
import ru.skypro.homework.dto.generatedDto.CommentsDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommentMapperTest {
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void shouldMapCommentToCommentDto() {
        //given
        User testUser = new User();
        testUser.setId(42L);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(testUser);
        comment.setText("Test comment");
        comment.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1, 1));

        //when
        CommentDto commentDto = commentMapper.commentToCommentDto(comment);
        System.out.println(commentDto);

        //then
        assertThat(commentDto).isNotNull();
        assertThat(commentDto.getPk()).isEqualTo(comment.getId().intValue());
        assertThat(commentDto.getAuthor()).isEqualTo(testUser.getId().intValue());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
        assertThat(commentDto.getCreatedAt()).isEqualTo(comment.getCreatedAt().toString());
    }

    @Test
    void shouldMapCommentDtoToComment() {
        //given
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(42);
        commentDto.setText("Test comment");
        commentDto.setPk(1);
        commentDto.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1, 1).toString());

        //when
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        System.out.println(comment);

        //then
        assertThat(comment).isNotNull();
        assertThat(comment.getCreatedAt().toString()).isEqualTo(commentDto.getCreatedAt());
        assertThat(comment.getText()).isEqualTo(commentDto.getText());
        assertThat(comment.getAd()).isNull();
        assertThat(comment.getId().intValue()).isEqualTo(commentDto.getPk());
        assertThat(comment.getAuthor().getId().intValue()).isEqualTo(commentDto.getAuthor());
    }

    @Test
    public void shouldMapCommentsListToCommentsDto() {
        //given
        User testUser = new User();
        testUser.setId(42L);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(testUser);
        comment.setText("Test comment");
        comment.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1, 1));

        List<Comment> commentList = List.of(comment);

        //when
        CommentsDto rwc = commentMapper.commentListToCommentsDto(commentList.size(), commentList);
        System.out.println(rwc);

        //then
        assertThat(rwc).isNotNull();
        assertThat(rwc.getCount()).isEqualTo(commentList.size());
        assertThat(rwc.getResults()).isNotNull();
        assertThat(rwc.getResults().get(0)).isEqualTo(commentMapper.commentToCommentDto(comment));
    }
}