package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class CreateOrUpdateComment {

    private String text; // Текст комментария

    public CreateOrUpdateComment(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreateOrUpdateComment that = (CreateOrUpdateComment) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }

    @Override
    public String toString() {
        return "CreateOrUpdateComment{" +
                "text='" + text + '\'' +
                '}';
    }
}
