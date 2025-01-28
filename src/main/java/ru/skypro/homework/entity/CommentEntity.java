package ru.skypro.homework.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "text")
    @Length(min = 8, max = 64)
    private String text;

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "user_name_create_comment")
    private String userNameCreateComment;

    @Column(name = "image_user_create_comment")
    private String imageUserCreateComment;

    @Column(name = "date_create_comment")
    private LocalDateTime dateCreateComment;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private AdEntity ad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text) && Objects.equals(ad, that.ad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, ad);
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", ad=" + ad +
                '}';
    }
}
