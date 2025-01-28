package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Класс, представляющий сущность "Объявление".
 * Используется для хранения информации об объявлениях.
 */
@Entity
@Table(name = "ad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad {

    /**
     * Уникальный идентификатор объявления (первичный ключ).
     * Генерируется автоматически при добавлении записи в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    /**
     * Ссылка на изображение объявления.
     * Поле обязательно для заполнения.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull(message = "Ссылка на изображение обязательна")
    private String image;

    /**
     * Цена объявления.
     * Поле обязательно для заполнения.
     * Цена не может быть отрицательной.
     */
    @Min(value = 0, message = "Цена не может быть отрицательной")
    @NotNull(message = "Цена обязательна")
    private String price;

    /**
     * Заголовок объявления.
     * Поле обязательно для заполнения.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull(message = "Заголовок обязателен")
    private String title;

    /**
     * Описание объявления.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Дата и время создания объявления.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Пользователь, создавший объявление.
     * Используется ленивая загрузка (FetchType.LAZY) для оптимизации.
     * Поле обязательно для заполнения (nullable = false).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}