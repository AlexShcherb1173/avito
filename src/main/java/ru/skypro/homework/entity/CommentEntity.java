package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(schema = "graduate", name = "comments")
public class CommentEntity {
    /**
     * Id комментария
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
     */
    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    /**
     * Текст комментария
     */
    @Column(name = "text", nullable = false)
    private String text;

    /**
     * Комментарии объявления
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private AdEntity ad;

    /**
     * Автор комментария
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;
}
