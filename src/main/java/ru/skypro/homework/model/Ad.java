package ru.skypro.homework.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ad {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank @Size(min = 10, max = 1000) 
    private String description;
    
    @NotBlank
    private String image;
    
    @PositiveOrZero 
    @NotNull(message = "Цена обязательна")
    private BigDecimal price; 
    
    @NotBlank @Size(min = 3, max = 100) 
    private String title;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return id == ad.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
