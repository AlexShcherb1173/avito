package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class Comment {

    private Integer author; // id автора комментария
    private String authorImage; // Ссылка на аватар автора комментария
    private String authorFirstName; // Имя создателя комментария
    private Integer createdAt; // Дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    private Integer pk; // id комментария
    private String text; // Текст комментария

    public Comment(Integer author, String authorImage, String authorFirstName, Integer createdAt, Integer pk, String text) {
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(author, comment.author) && Objects.equals(authorImage, comment.authorImage) && Objects.equals(authorFirstName, comment.authorFirstName) && Objects.equals(createdAt, comment.createdAt) && Objects.equals(pk, comment.pk) && Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, authorImage, authorFirstName, createdAt, pk, text);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "author=" + author +
                ", authorImage='" + authorImage + '\'' +
                ", authorFirstName='" + authorFirstName + '\'' +
                ", createdAt=" + createdAt +
                ", pk=" + pk +
                ", text='" + text + '\'' +
                '}';
    }
}
