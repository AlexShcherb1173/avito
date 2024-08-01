package ru.skypro.homework.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User author;
    private BigDecimal price;
    private String title;

    @OneToMany
    private Image image;
    private String description;
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<Comment> comments;

}
