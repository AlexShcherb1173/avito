package ru.skypro.homework.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @Column(name = "TEXT")
    private String text;
    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.EAGER)

    private UserEntity user;
    @JoinColumn(name = "AD_ID")
    @ManyToOne(fetch = FetchType.LAZY)

    private AdEntity ad;
}
