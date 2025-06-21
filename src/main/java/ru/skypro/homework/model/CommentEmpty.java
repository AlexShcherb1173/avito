package ru.skypro.homework.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class CommentEmpty {

    private int author;
    private String authorImage;
    private String authorFirstName;
    private long createdAt;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pk;
    private String text;

}
