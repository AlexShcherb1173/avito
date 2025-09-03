package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 64)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}