package ru.skypro.homework.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * {@link Класс} Ad объявления <br>
 * {@link Integer} id - идентификатор объявления (not null) <br>
 * {@link String} title - титульник объявления (not null) <br>
 * {@link String} description - содержание объявления (not null) <br>
 * {@link Integer} price  - цена товара (not null) <br>
 * {@link Byte} image - фото товара (not null) <br>
 * {@link User} user - автор объявления (not null) <br>
 * {@link List<Comment>} commentList - список комментариев для объявления <br>
 *
 * @author Archinski
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String title;
    private String description;
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_image_id", referencedColumnName = "id")
    private Image image;
}
