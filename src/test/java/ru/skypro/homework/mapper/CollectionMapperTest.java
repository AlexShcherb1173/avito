package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollectionMapperTest {

    @Mock
    private AdMapper adMapper;

    @Mock
    private CommentMapper commentMapper;

    private CollectionMapper collectionMapper;

    @BeforeEach
    void setUp() {
        collectionMapper = new CollectionMapper(adMapper, commentMapper);
    }

    @Test
    void adsToDto_WhenListNotNull_ShouldReturnListOfAdDtos() {
        // Given
        AdEntity ad1 = new AdEntity();
        ad1.setId(1);

        AdEntity ad2 = new AdEntity();
        ad2.setId(2);

        List<AdEntity> entities = Arrays.asList(ad1, ad2);

        AdDto dto1 = new AdDto();
        dto1.setPk(1);
        dto1.setTitle("Ad 1");
        dto1.setPrice(1000);

        AdDto dto2 = new AdDto();
        dto2.setPk(2);
        dto2.setTitle("Ad 2");
        dto2.setPrice(2000);

        when(adMapper.toDto(ad1)).thenReturn(dto1);
        when(adMapper.toDto(ad2)).thenReturn(dto2);

        // When
        List<AdDto> result = collectionMapper.adsToDto(entities);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        assertEquals(1, result.get(0).getPk());
        assertEquals("Ad 1", result.get(0).getTitle());
        assertEquals(1000, result.get(0).getPrice());

        assertEquals(2, result.get(1).getPk());
        assertEquals("Ad 2", result.get(1).getTitle());
        assertEquals(2000, result.get(1).getPrice());
    }

    @Test
    void adsToDto_WhenListNull_ShouldReturnNull() {
        // When
        List<AdDto> result = collectionMapper.adsToDto(null);

        // Then
        assertNull(result);
    }

    @Test
    void adsToDto_WhenListEmpty_ShouldReturnEmptyList() {
        // Given
        List<AdEntity> entities = Collections.emptyList();

        // When
        List<AdDto> result = collectionMapper.adsToDto(entities);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void commentsToDto_WhenListNotNull_ShouldReturnListOfCommentDtos() {
        // Given
        CommentEntity comment1 = new CommentEntity();
        comment1.setId(100);

        CommentEntity comment2 = new CommentEntity();
        comment2.setId(200);

        List<CommentEntity> entities = Arrays.asList(comment1, comment2);

        CommentDto dto1 = new CommentDto();
        dto1.setPk(1);
        dto1.setAuthor(11);
        dto1.setText("test text 1");

        CommentDto dto2 = new CommentDto();
        dto2.setPk(2);
        dto2.setAuthor(12);
        dto2.setText("test text 2");

        when(commentMapper.toDto(comment1)).thenReturn(dto1);
        when(commentMapper.toDto(comment2)).thenReturn(dto2);

        // When
        List<CommentDto> result = collectionMapper.commentsToDto(entities);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        assertEquals(1, dto1.getPk());
        assertEquals(11, dto1.getAuthor());
        assertEquals("test text 1", dto1.getText());

        assertEquals(2, dto2.getPk());
        assertEquals(12, dto2.getAuthor());
        assertEquals("test text 2", dto2.getText());
    }

    @Test
    void commentsToDto_WhenListNull_ShouldReturnNull() {
        // When
        List<CommentDto> result = collectionMapper.commentsToDto(null);

        // Then
        assertNull(result);
    }

    @Test
    void toAdsDto_ShouldReturnAdsDtoWithCountAndResults() {
        // Given
        AdEntity ad1 = new AdEntity();
        AdEntity ad2 = new AdEntity();
        List<AdEntity> entities = Arrays.asList(ad1, ad2);

        AdDto dto1 = new AdDto();
        AdDto dto2 = new AdDto();
        List<AdDto> dtos = Arrays.asList(dto1, dto2);

        when(adMapper.toDto(ad1)).thenReturn(dto1);
        when(adMapper.toDto(ad2)).thenReturn(dto2);

        // When
        AdsDto result = collectionMapper.toAdsDto(entities);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResults().size());
        assertEquals(dto1, result.getResults().get(0));
        assertEquals(dto2, result.getResults().get(1));
    }

    @Test
    void toCommentsDto_ShouldReturnCommentsDtoWithCountAndResults() {
        // Given
        CommentEntity comment1 = new CommentEntity();
        CommentEntity comment2 = new CommentEntity();
        List<CommentEntity> entities = Arrays.asList(comment1, comment2);

        CommentDto dto1 = new CommentDto();
        CommentDto dto2 = new CommentDto();
        List<CommentDto> dtos = Arrays.asList(dto1, dto2);

        when(commentMapper.toDto(comment1)).thenReturn(dto1);
        when(commentMapper.toDto(comment2)).thenReturn(dto2);

        // When
        CommentsDto result = collectionMapper.toCommentsDto(entities);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResults().size());
        assertEquals(dto1, result.getResults().get(0));
        assertEquals(dto2, result.getResults().get(1));
    }
}