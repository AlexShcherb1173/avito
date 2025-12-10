package ru.skypro.homework.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * JPA сущность для представления объявления в базе данных.
 * Соответствует таблице 'ads' в базе данных.
 * Содержит информацию об объявлении и его связи с пользователем и комментариями.
 */
@Entity
@Table(name = "ads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdEntity {

    /**
     * Уникальный идентификатор объявления.
     * Генерируется автоматически базой данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Заголовок объявления.
     * Не может быть null, максимальная длина 32 символа.
     */
    @Column(name = "title", nullable = false, length = 32)
    private String title;

    /**
     * Цена товара/услуги в объявлении.
     * Не может быть null.
     */
    @Column(name = "price", nullable = false)
    private Integer price;

    /**
     * Подробное описание объявления.
     * Может быть null, максимальная длина 64 символа.
     */
    @Column(name = "description", length = 64)
    private String description;

    /**
     * Имя файла изображения объявления.
     * Хранится в виде строки, может быть null.
     */
    @Column(name = "image")
    private String image;

    /**
     * Автор объявления.
     * Многосторонняя связь Many-to-One с сущностью UserEntity.
     * Не может быть null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    /**
     * Список комментариев к объявлению.
     * Однонаправленная связь One-to-Many с каскадными операциями.
     */
    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommentEntity> comments;
}