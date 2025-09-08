package ru.skypro.homework.mapper;


import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.enity.Ad;
import ru.skypro.homework.enity.Comment;
import ru.skypro.homework.enity.User;
import java.time.LocalDateTime;


@Component
public final class CommentMapper {
    private CommentMapper() {}

    public static CommentDto toDto(Comment c) {
        if (c == null) return null;

        CommentDto dto = new CommentDto();
        dto.setId(c.getId() == null ? null : c.getId().intValue());
        dto.setText(c.getText());
        dto.setAuthor(c.getAuthor() != null ? c.getAuthor().getUsername() : null);
        dto.setCreatedAt(c.getCreatedAt() != null ? c.getCreatedAt().toString() : null);
        return dto;
    }


    public static Comment toEntityForCreate(String text, Ad ad, User author) {
        Comment c = new Comment();
        c.setText(text);
        c.setAd(ad);
        c.setAuthor(author);
        c.setCreatedAt(LocalDateTime.now());
        return c;
    }


    public static void updateEntity(Comment c, CommentDto dto) {
        if (c == null || dto == null) return;
        c.setText(dto.getText());
    }
}