package ru.skypro.homework.mapper;


import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Data
@Transactional
@ExtendWith(MockitoExtension.class)
class CommentMapperTest {
    private final CommentMapper commentMapper= Mappers.getMapper(CommentMapper.class);

    @Test
    void toDTO_shouldMapCommentToCommentDto() {
        //given
        User testUser = new User();
        testUser.setId(100);
        Comment comment = new Comment();
        comment.setId(200);
        comment.setUser(testUser);
        comment.setText("Test comment");
        comment.setCreatedAt(LocalDateTime.of(1,1,1,1,1));


        //when
        CommentDto commentDto = commentMapper.toDTO(comment);
        System.out.println(commentDto);

        //then
        assertThat(commentDto).isNotNull();
        assertThat(commentDto.getPk()).isEqualTo(comment.getId().intValue());
        assertThat(commentDto.getAuthor()).isEqualTo(testUser.getId().intValue());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
//        assertThat(commentDto.getCreatedAt()).isEqualTo(comment.getCreatedAt().toString());TODO
    }

    @Test
    void toCommentsDTO_shouldMapCommentsListToComments() {
        //given
        User testUser = new User();
        testUser.setId(100);
        Comment comment = new Comment();
        comment.setId(200);
        comment.setUser(testUser);
        comment.setText("Test comment");
        comment.setCreatedAt(LocalDateTime.of(1,1,1,1,1));
        List<Comment> commentList = List.of(comment);
        //when
        Comments comments = commentMapper.toCommentsDTO(commentList.size(), commentList);
        System.out.println(comments);

        //then
        assertThat(comments).isNotNull();
        assertThat(comments.getCount()).isEqualTo(commentList.size());
        assertThat(comments.getResults()).isNotNull();
        assertThat(comments.getResults().get(0)).isEqualTo(commentMapper.toDTO(comment));
    }

    @Test
    void toDTO3_shouldMapCommentDtoToComment() {
        //given
        User testUser = new User();
        testUser.setId(100);
        Comment comment = new Comment();
        comment.setId(200);
        comment.setUser(testUser);
        comment.setText("Test comment");
        comment.setCreatedAt(LocalDateTime.of(1,1,1,1,1));

        //when
        CreateOrUpdateComment createOrUpdateComment = commentMapper.toDTO3(comment);
        System.out.println(createOrUpdateComment);

        //then
        assertThat(createOrUpdateComment).isNotNull();

        assertThat(createOrUpdateComment.getText()).isEqualTo(comment.getText());

    }
}