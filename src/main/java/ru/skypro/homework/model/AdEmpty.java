package ru.skypro.homework.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class AdEmpty {

    private int author;
    private String image;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pk;
    private int price;
    private String title;

}
