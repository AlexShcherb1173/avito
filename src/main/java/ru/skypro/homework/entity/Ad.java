package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "ads")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных объявления")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 4, max = 32)
    private String title;

    private Integer price;

    @Size(min = 8, max = 64)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id")
    private Picture image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY)
    private Collection<Comment> comments;
}
