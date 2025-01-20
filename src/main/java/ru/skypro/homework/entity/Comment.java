package ru.skypro.homework.entity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ads ad;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    private Long createdAt;
    private String text;

}