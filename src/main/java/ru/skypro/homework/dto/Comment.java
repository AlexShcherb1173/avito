package ru.skypro.homework.dto;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    private int author;
    private String authorImage;
    private String authorFirstName;
    private long createdAt;
    private String text;

    @Column(name = "ad_id")
    private int adId;
}
