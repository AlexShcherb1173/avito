package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ads")
@EntityListeners(AuditingEntityListener.class)
public class Ad extends ParentIDEntity {

    /**
     * цена объявления
     */
    @Column(name = "price")
    private Integer price;
    /**
     * заголовок объявления
     */
    @Column(name = "title")
    private String title;
    /**
     * описание объявления
     */
    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    @CreatedBy
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    private List<Comment> comments;

    @Builder
    public Ad(Long id, Integer price, String title, String description, Image image, User user, List<Comment> comments) {
        super(id);
        this.price = price;
        this.title = title;
        this.description = description;
        this.image = image;
        this.user = user;
        this.comments = comments;
    }
}
