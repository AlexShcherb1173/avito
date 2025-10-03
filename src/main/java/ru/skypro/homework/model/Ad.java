package ru.skypro.homework.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    private String title;
    private Integer price;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private String image;
}
