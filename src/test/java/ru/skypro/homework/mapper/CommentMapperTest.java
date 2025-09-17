package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CommentMapperTest {

    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void toComment_createsEntityWithGeneratedFields() {
        CommentCreateOrUpdateRequest commentCreateOrUpdateRequest = new CommentCreateOrUpdateRequest("Комментарий");
        Ad ad = new Ad();

        Comment comment = mapper.toComment(commentCreateOrUpdateRequest, ad);

        assertEquals("Комментарий", comment.getText());
        assertSame(ad, comment.getAd());
        assertNotNull(comment.getCreatedAt());
        assertEquals(0L, comment.getId());
    }
}
