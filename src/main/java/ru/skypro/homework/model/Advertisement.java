package ru.skypro.homework.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Double price;
    private String image;
    @ManyToOne
    private User author;
}
