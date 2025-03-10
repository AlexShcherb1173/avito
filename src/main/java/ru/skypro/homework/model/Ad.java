package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @Min(value = 0, message = "Цена не может быть отрицательной")
    @NotNull(message = "Цена обязательна")
    private Integer price;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull(message = "Заголовок обязателен")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<Comment> comment;
}