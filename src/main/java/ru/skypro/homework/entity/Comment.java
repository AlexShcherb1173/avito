package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>Класс {@link Comment} представляет собой сущность комментария в системе. </b> <p>
 * Он содержит информацию о комментарии, включая текст, автора и связанный объект объявления.
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    /**
     * <b>Уникальный идентификатор комментария. </b> <p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * <b>Дата и время создания комментария. </b> <p>
     */
    private LocalDateTime createdAt;

    /**
     * <b>Текст комментария. </b> <p>
     */
    private String text;

    /**
     * <b>Автор комментария. Связь с сущностью {@link User}. </b> <p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;


    /**
     * <b>Объявление, к которому относится комментарий. Связь с сущностью {@link Listing}. </b> <p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pk_listing")
    private Listing listing;



}
