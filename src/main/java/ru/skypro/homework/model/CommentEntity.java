package ru.skypro.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comment_model")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId; // ид комментария

    private Integer author; // ид автора объявления
    private String authorImage; // ссылка на аватар автора
    private String authorFirstName; // имя создателя комментария
    private Integer createAd; //дата и время создания комментарий
    private String text; //текст комментарий

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private AdEntity ad;

    public UserEntity getOwner() {
        return this.user; //Возвращаем объект пользователя, который является владельцем объявления
    }
}
