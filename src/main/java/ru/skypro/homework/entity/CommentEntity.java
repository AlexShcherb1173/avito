package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments", indexes = {
        @Index(name = "ix_comments_ad_id", columnList = "ad_id"),
        @Index(name = "ix_comments_author_id", columnList = "author_id")
})
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4000)
    private String text;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private AdEntity ad;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
    }
}
