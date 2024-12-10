package ru.skypro.homework.model;

import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.UserDto;

import javax.persistence.*;

@Entity
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer pk;

    private String text;

    @Column(name = "createdat")
    private Integer createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;

}
