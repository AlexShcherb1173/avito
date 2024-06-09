package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
public class Comment extends ParentIDEntity {

    /**
     * ссылка на автора комментария
     */
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    @CreatedBy
    private User user;

    /**
     * дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
     */
    @Column(name = "createdAt", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * текст комментария
     */
    @Column(name = "text")
    private String text;

    @Builder
    public Comment(Long id, String text) {
        super(id);
        this.text = text;
    }
}
