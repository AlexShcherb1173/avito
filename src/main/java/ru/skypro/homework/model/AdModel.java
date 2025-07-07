package ru.skypro.homework.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ads")
public class AdModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;	        // id объявления

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserModel author;        // Автор объявления

    private String image;	    // ссылка на картинку объявления
    private int price;	        // цена объявления
    private String title;	    // заголовок объявления
    private String description;	// описание объявления

    @OneToMany(mappedBy = "adModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CommentModel> commentModels; // Комментарии к объявлению
}
