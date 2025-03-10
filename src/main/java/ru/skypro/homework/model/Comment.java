package ru.skypro.homework.model;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Класс, представляющий собой сущность комментария.
 *
 * Эта сущность содержит информацию о комментарии, включая его уникальный идентификатор,
 * автора, объявление, дату создания и текст комментария.
 *
 * Поле {@code pk} содержит уникальный идентификатор комментария.
 * Поле {@code author} представляет автора комментария, связанного с пользователем (связь многие к одному).
 * Поле {@code ad} представляет объявление, к которому привязан комментарий (связь многие к одному).
 * Поле {@code createdAt} хранит дату и время создания комментария в миллисекундах с 01.01.1970.
 * Поле {@code text} содержит текст комментария.
 *
 * Аннотация {@link ManyToOne} устанавливает связь с сущностями {@link User} (автор) и {@link Ad} (объявление).
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @Column(name = "created_at")
    private Long createdAt;

    private String text;

}
