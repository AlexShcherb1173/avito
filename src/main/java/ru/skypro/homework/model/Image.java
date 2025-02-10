package ru.skypro.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Этот класс аннотирован как сущность JPA с помощью аннотации {@code @Entity},
 * что позволяет сохранять объекты Image в базе данных. Используется таблица "image"
 * для хранения данных.
 * </p>
 * <p>
 * Аннотация {@code @Data} от Lombok автоматически генерирует методы для доступа к полям,
 * такие как геттеры и сеттеры, а также методы {@code equals}, {@code hashCode} и {@code toString}.
 * </p>
 * <p>
 * Конструктор без аргументов создается с помощью аннотации {@code @NoArgsConstructor}.
 * </p>
 * <p>
 * Поле {@code id} представляет уникальный идентификатор изображения и аннотировано
 * как первичный ключ с автоматической генерацией значений.
 * </p>
 * <p>
 * Поле {@code imagePath} содержит путь к изображению.
 * </p>
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "image_path")
    private String imagePath;
}
