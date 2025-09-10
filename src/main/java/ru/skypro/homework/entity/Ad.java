package ru.skypro.homework.entity;

import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 64)
    private String description;

    private String imageUrl;
    // Путь к файлу на диске, состоящий только из имени файла (без имени папки и /)

    // Если будет LazyException, то переделать на графы
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    // cascade = CascadeType.ALL - наличие данного выражения обеспечит удаление всех комментариев при удалении
    // объявления
    // orphanRemoval = true - если этот параметр выставлен в true, то дочерняя сущность будет удалена, если на нее
    // исчезли все ссылки. Если несколько родительских сущностей ссылаются на одну дочернюю, то выгодно, чтобы она
    // удалялась не вместе с удалением родительской сущности, а только если все ссылки на нее будут обнулены.
}
