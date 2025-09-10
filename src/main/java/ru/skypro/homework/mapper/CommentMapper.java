package ru.skypro.homework.mapper;


import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.enity.Ad;
import ru.skypro.homework.enity.Comment;
import ru.skypro.homework.enity.User;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Component
public final class CommentMapper {
    private CommentMapper() {}

    public static CommentDto toDto(Comment c) {
        if (c == null) return null;

        CommentDto dto = new CommentDto();
        dto.setPk(c.getId() == null ? null : c.getId().intValue());
        dto.setAuthor(c.getAuthor() != null ? c.getAuthor().getId().intValue() : null);
        dto.setAuthorFirstName(c.getAuthor() != null ? c.getAuthor().getFirstName() : null);
        dto.setAuthorImage(c.getAuthor() != null && c.getAuthor().getImageUrl() != null
                ? "/users/" + c.getAuthor().getId() + "/image" : null);
        dto.setCreatedAt(c.getCreatedAt() != null ? c.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli() : null);
        dto.setText(c.getText());
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