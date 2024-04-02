package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;


@Entity
@Data


@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CREATED_AT")

    private LocalDateTime createdAt;

    @Column(name = "TEXT")
    private String text;
    @ManyToOne
    @JoinColumn(name = "ADS_ID")
    private Ads ads;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;





}
