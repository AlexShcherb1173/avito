package ru.avito.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void shouldCreateCommentWithBuilder() {
        User user = User.builder().id(1).build();
        Ad ad = Ad.builder().id(1).build();

        Instant now = Instant.now();

        Comment comment = Comment.builder()
                .id(1)
                .text("Test comment")
                .createdAt(now)
                .author(user)
                .ad(ad)
                .build();

        assertEquals(1, comment.getId());
        assertEquals("Test comment", comment.getText());
        assertEquals(now, comment.getCreatedAt());
        assertEquals(user, comment.getAuthor());
        assertEquals(ad, comment.getAd());
    }

    @Test
    void shouldTestEquals() {
        Comment c1 = Comment.builder().id(1).text("A").build();
        Comment c2 = Comment.builder().id(1).text("A").build();

        assertEquals(c1, c2);
    }

    @Test
    void shouldBuildCommentCorrectly() {
        User user = User.builder().id(1).build();
        Ad ad = Ad.builder().id(10).build();

        Instant now = Instant.now();

        Comment comment = Comment.builder()
                .id(5)
                .text("Hello")
                .createdAt(now)
                .author(user)
                .ad(ad)
                .build();

        assertEquals(5, comment.getId());
        assertEquals("Hello", comment.getText());
        assertEquals(now, comment.getCreatedAt());
        assertEquals(user, comment.getAuthor());
        assertEquals(ad, comment.getAd());
    }
}