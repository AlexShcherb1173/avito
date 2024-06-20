package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;
    private int price;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity author;

    public Ad(UserEntity author, String title, int price, String description) {
        this.author = author;
        this.title = title;
        this.price = price;
        this.description = description;
    }
}
