package ru.skypro.homework.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long pk;

    private int author;
    private String authorImage;
    private String authorFirstName;
    private long createdAt;
    private String text;

}
