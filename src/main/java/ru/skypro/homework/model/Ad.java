package ru.skypro.homework.model;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

/**
 * Класс, представляющий собой сущность объявления.
 *
 * Эта сущность содержит информацию о самом объявлении, включая его уникальный идентификатор,
 * автора, изображение, цену, заголовок, описание и комментарии.
 *
 * Поле {@code pk} содержит уникальный идентификатор объявления.
 * Поле {@code author} представляет автора объявления, связанного с пользователем (связь многие к одному).
 * Поле {@code image} хранит ссылку на изображение объявления.
 * Поле {@code price} указывает цену объявления.
 * Поле {@code title} содержит заголовок объявления.
 * Поле {@code description} содержит описание объявления.
 * Поле {@code comments} представляет список комментариев к данному объявлению (связь один ко многим).
 *
 * Аннотация {@link ManyToOne} устанавливает связь с сущностью {@link User}, представляющей автора объявления.
 * Аннотация {@link OneToMany} устанавливает связь с сущностью {@link Comment}, представляющей комментарии к объявлению.
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    private String image;

    private Integer price;

    private String title;

    private String description;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}
