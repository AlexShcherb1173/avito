package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@Table(name = "ads")
public class AdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "DESCRIPTION")
    private String description;
    @PositiveOrZero
    @Column(name = "PRICE")
    private int price;
    @Column(name = "TITLE")
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_ID")

    private ImageEntity image;
}
