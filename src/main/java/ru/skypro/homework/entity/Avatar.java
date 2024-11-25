package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <b>Класс, представляющий аватар пользователя. </b> <p>
 *
 * <p>Аватар содержит информацию о файле изображения, включая его путь, размер,
 * тип медиа и данные в виде массива байтов.</p>
 *
 * <p>Аннотация {@code @Entity} указывает, что этот класс является сущностью JPA,
 * а аннотация {@code @Id} обозначает, что поле {@code id} является уникальным
 * идентификатором для экземпляра класса.</p>
 *
 * <p>Использование аннотаций Lombok позволяет автоматически генерировать
 * геттеры, сеттеры, методы {@code equals}, {@code hashCode}, а также конструкторы
 * без параметров и со всеми параметрами.</p>
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {

    /**
     * <b>Уникальный идентификатор аватара. </b> <p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * <b>Путь к файлу аватара. </b> <p>
     */
    private String filePath;

    /**
     * <b>Размер файла аватара в байтах. </b> <p>
     */
    private long fileSize;

    /**
     * <b>Тип медиафайла (например, image/jpeg). </b> <p>
     */
    private String mediaType;

    /**
     * <b>Данные аватара в виде массива байтов. </b> <p>
     * <p>Это поле игнорируется при сериализации в JSON.</p>
     */
    @JsonIgnore
    private byte[] data;

}
