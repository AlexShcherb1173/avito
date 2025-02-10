package ru.skypro.homework.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Archinski
 */
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "comments")
public class Comment {

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Column(name = "author_image")
    private String authorImage;

    @Column(name = "author_first_name")
    private String authorFirstName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk")
    private Integer pk;

    @Column(name = "text")
    private String text;

    public Comment() {

    }

    @ManyToOne
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    private Ad ad;
}
