package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.model.Ad;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdCommentsMapperTest {

    private final AdCommentsMapper mapper = Mappers.getMapper(AdCommentsMapper.class);

    @Test
    void toCommentsDto_handlesEmptyCommentsList() {
        Ad ad = new Ad();
        ad.setComments(Collections.emptyList());

        CommentsDto dto = mapper.toCommentsDto(ad);

        assertEquals(0, dto.getCount());
        assertNotNull(dto.getResults());
        assertEquals(0, dto.getResults().size());
    }

}
