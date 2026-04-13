package ru.avito.dto.comment;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentsResponseTest {

    @Test
    void shouldUseConstructorsAndSetters() {
        CommentDto dto = new CommentDto(1, "/img.jpg", "Ivan", 123L, 10, "Comment text");

        CommentsResponse first = new CommentsResponse(1, List.of(dto));
        assertEquals(1, first.getCount());
        assertEquals(1, first.getResults().size());
        assertEquals(dto, first.getResults().get(0));

        CommentsResponse second = new CommentsResponse();
        second.setCount(1);
        second.setResults(List.of(dto));

        assertEquals(1, second.getCount());
        assertEquals(1, second.getResults().size());
        assertEquals(dto, second.getResults().get(0));
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        CommentDto dto = new CommentDto(1, "/img.jpg", "Ivan", 123L, 10, "Comment text");

        CommentsResponse first = new CommentsResponse(1, List.of(dto));
        CommentsResponse second = new CommentsResponse(1, List.of(dto));

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("count=1"));
    }
}