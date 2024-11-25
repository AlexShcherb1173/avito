package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.*;

/**
 * <b>Представляет сущность объявления в приложении. </b> <p>
 * Объявление содержит такие детали, как название, описание, цена,
 * и связано с автором и необязательным изображением.
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

    /**
     * <b>Уникальный идентификатор объявления. </b> <p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * <b>Описание объявления. </b> <p>
     */
    private String description;

    /**
     * <b>Цена объявления. </b> <p>
     */
    private Integer price;

    /**
     * <b>Название объявления. </b> <p>
     */
    private String title;

    /**
     * <b>Автор объявления. </b> <p>
     * Это связь многие-к-одному, где один пользователь может иметь несколько объявлений.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    /**
     * <b>Изображение, связанное с объявлением. </b> <p>
     * Это связь один-к-одному.
     */
    @OneToOne
    private Image image;
}
