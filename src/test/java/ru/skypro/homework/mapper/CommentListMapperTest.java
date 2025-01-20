package ru.skypro.homework.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommentListMapperTest {

    public static final int FIRST_COMMENT_ID = 444;
    public static final int SECOND_COMMENT_ID = 4444;
    public static final int THIRD_COMMENT_ID = 44;
    public static final int FOURTH_COMMENT_ID = 4;
    public static final int FIRST_USER_ID = 222;
    public static final int SECOND_USER_ID = 2;
    public static final String FIRST_USER_FIRST_NAME = "firstName";
    public static final String SECOND_USER_FIRST_NAME = "wrongFirstName";
    public static final String FIRST_PATH_TO_IMAGE = "filePath";
    public static final String SECOND_PATH_TO_IMAGE = "wrongFilePath";
    public static final int FIRST_AD_ID = 333;
    public static final int SECOND_AD_ID = 3;

    private final CommentListMapper commentListMapper;

    CommentListMapperTest(CommentListMapper commentListMapper) {
        this.commentListMapper = commentListMapper;
    }

    @Test
    void toDto() {

        Ads firstAd = new Ads();
        firstAd.setPk(FIRST_AD_ID);

        Ads secondAd = new Ads();
        secondAd.setPk(SECOND_AD_ID);

        Image firstImage = new Image();
        firstImage.setFilePath(FIRST_PATH_TO_IMAGE);

        Image secondImage = new Image();
        secondImage.setFilePath(SECOND_PATH_TO_IMAGE);

        User firstUser = new User();
        firstUser.setId(FIRST_USER_ID);
        firstUser.setFirstName(FIRST_USER_FIRST_NAME);
        firstUser.setImage(firstImage);

        User secondUser = new User();
        secondUser.setId(SECOND_USER_ID);
        secondUser.setFirstName(SECOND_USER_FIRST_NAME);
        secondUser.setImage(secondImage);

        Comment firstComment = new Comment();
        firstComment.setCommentId(FIRST_COMMENT_ID);
        firstComment.setAd(firstAd);
        firstComment.setAuthor(firstUser);

        Comment secondComment = new Comment();
        secondComment.setCommentId(SECOND_COMMENT_ID);
        secondComment.setAd(firstAd);
        secondComment.setAuthor(secondUser);

        Comment thirdComment = new Comment();
        thirdComment.setCommentId(THIRD_COMMENT_ID);
        thirdComment.setAd(firstAd);
        thirdComment.setAuthor(firstUser);

        Comment fourthComment = new Comment();
        fourthComment.setCommentId(FOURTH_COMMENT_ID);
        fourthComment.setAd(secondAd);
        fourthComment.setAuthor(secondUser);

        firstAd.setCommentsList(List.of(firstComment,secondComment,thirdComment));
        secondAd.setCommentsList(List.of(fourthComment));

        ResponseWrapperCommentDto dto = commentListMapper.toResponseWrapperCommentDto(firstAd);

        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getCount()).isEqualTo(3);
        Assertions.assertThat(dto.getResults().get(0)).isInstanceOf(CommentDto.class);
    }
}