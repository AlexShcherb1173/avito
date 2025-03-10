package ru.skypro.homework.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Table(name = "comments")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Data
//@AllArgsConstructor
//@Entity
//public class CommentEntity {

//    /*** Comment Id / Уникальный идентификатор комментария ***/
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Integer id;
//
//    /*** Date&Time of comment creation / Дата и время создания комментария ***/
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    /*** Comment text content / Текст комментария ***/
//    @Column(name = "text")
//    private String text;
//
//    /*** Comment author / Автор комментария ***/
//    @ManyToOne
//    private UserEntity user;
//
//    /*** Ad data model / Модель данных объявления ***/
//    @ManyToOne
//    private AdEntity ad;
//
//    public CommentEntity() {
//    }
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//private Integer pk;
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//    private String text;
//    @ManyToOne(fetch = FetchType.LAZY)
//    private UserEntity user;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ad_id")
//    private AdEntity ad;
//
//
//    public CommentEntity() {
//
//    }
//}

@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@Entity
public class CommentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private UserEntity author;
    private LocalDateTime createdAt;
    private String text;
    @ManyToOne
    private AdEntity ad;

    public CommentEntity() {

    }
}
