package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
//@Table(name = "image")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    /**
     * Уникальный идентификатор изображения (первичный ключ).
     * Генерируется автоматически при добавлении записи в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Путь к изображению в файловой системе или URL.
     * Поле обязательно для заполнения (валидация через @NotNull).
     */
    @Column(nullable = false)
    @NotNull(message = "Путь к изображению обязателен")
    private String path;

    /**
     * Бинарные данные изображения (опционально).
     * Используется тип LONGBLOB для хранения больших объектов.
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    public Long getId() {
        return id;
    }
}