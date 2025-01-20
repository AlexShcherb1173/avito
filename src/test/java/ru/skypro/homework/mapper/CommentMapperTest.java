package ru.skypro.homework.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    public static final int COMMENT_ID = 444;
    public static final int WRONG_COMMENT_ID = 4;
    public static final String TEXT = "Some text";
    public static final String NEW_TEXT = "Some other text";
    public static final long DATE_MILLIS = 123456789L;
    public static final long WRONG_DATE_MILLIS = 987654321L;
    public static final int USER_ID = 222;
    public static final int WRONG_USER_ID = 2;
    public static final String USER_FIRST_NAME = "firstName";
    public static final String WRONG_USER_FIRST_NAME = "wrongFirstName";
    public static final String WRONG_IMAGE_URL = "Some wrong URL";
    public static final int IMAGE_ID = 1111;

    private final CommentMapper commentMapper;

    CommentMapperTest(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Test
    void toDtoTest() {

        Image image = new Image();
        image.setId(IMAGE_ID);

        User user = new User();
        user.setId(USER_ID);
        user.setFirstName(USER_FIRST_NAME);
        user.setImage(image);

        Comment comment = new Comment();
        comment.setAd(new Ads());
        comment.setCommentId(COMMENT_ID);
        comment.setAuthor(user);
        comment.setCreatedAt(DATE_MILLIS);
        comment.setText(TEXT);

        CommentDto dto = commentMapper.toDto(comment);

        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getPk()).isEqualTo(COMMENT_ID);
        Assertions.assertThat(dto.getText()).isEqualTo(TEXT);
        Assertions.assertThat(dto.getCreatedAt()).isEqualTo(DATE_MILLIS);
        Assertions.assertThat(dto.getAuthor()).isEqualTo(USER_ID);
        Assertions.assertThat(dto.getAuthorFirstName()).isEqualTo(USER_FIRST_NAME);
        Assertions.assertThat(dto.getAuthorImage()).isEqualTo("/image/" + IMAGE_ID);

    }

    @Test
    void updateCommentTest() {

        Image image = new Image();

        User user = new User();
        user.setFirstName(USER_FIRST_NAME);
        user.setImage(image);

        Comment comment = new Comment();
        comment.setAd(new Ads());
        comment.setCommentId(COMMENT_ID);
        comment.setAuthor(user);
        comment.setCreatedAt(DATE_MILLIS);
        comment.setText(TEXT);

        CommentDto dto = new CommentDto();
        dto.setPk(WRONG_COMMENT_ID);
        dto.setAuthor(WRONG_USER_ID);
        dto.setAuthorFirstName(WRONG_USER_FIRST_NAME);
        dto.setAuthorImage(WRONG_IMAGE_URL);
        dto.setCreatedAt(WRONG_DATE_MILLIS);
        dto.setText(NEW_TEXT);

        commentMapper.updateComment(dto, comment);

        Assertions.assertThat(comment).isNotNull();
        Assertions.assertThat(comment.getCommentId()).isEqualTo(COMMENT_ID);
        Assertions.assertThat(comment.getAuthor()).isEqualTo(user);
        Assertions.assertThat(comment.getCreatedAt()).isEqualTo(DATE_MILLIS);
        Assertions.assertThat(comment.getText()).isEqualTo(NEW_TEXT);

    }

}