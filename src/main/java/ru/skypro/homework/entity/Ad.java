package ru.skypro.homework.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import ru.skypro.homework.entity.User;


@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User author; // множество объявлений - один автор
    private BigDecimal price;
    private String title;
    @OneToOne
    private Image image; // одно объявления - множество изображений
    private String description;
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<Comment> comments; // одно объявление - множество комментариев
}
