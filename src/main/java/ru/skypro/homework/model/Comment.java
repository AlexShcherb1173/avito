package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.Instant;

/**
 * Класс, представляющий сущность "Комментарий".
 * Используется для хранения комментариев к объявлениям.
 */
@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    /**
     * Уникальный идентификатор комментария (первичный ключ).
     * Генерируется автоматически при добавлении записи в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Текст комментария.
     * Может быть длинным, поэтому используется тип TEXT.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    /**
     * Дата и время создания комментария.
     * Хранится в формате Unix-времени (миллисекунды с 01.01.1970).
     */
    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    /**
     * Пользователь, оставивший комментарий.
     * Используется ленивая загрузка (FetchType.LAZY) для оптимизации.
     * Поле обязательно для заполнения (nullable = false).
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Объявление, к которому относится комментарий.
     * Используется ленивая загрузка (FetchType.LAZY) для оптимизации.
     * Поле обязательно для заполнения (nullable = false).
     */
    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;
}