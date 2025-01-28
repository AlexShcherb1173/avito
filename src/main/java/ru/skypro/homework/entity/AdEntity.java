package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "ad")
public class AdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    @Length(min = 8, max = 64)
    private String title;

    @Column(name = "description")
    @Length(min = 4, max = 32)
    private String target;

    @Column(name = "price")
    @Length(max = 10000000)
    private Integer price;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "last_name_user")
    private String lastNameUser;

    @Column(name = "first_name_user")
    private String firstNameUser;

    @Column(name = "name_user")
    private String nameUser;

    @Column(name = "phone_user")
    private String phoneUser;

    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdEntity adEntity = (AdEntity) o;
        return Objects.equals(id, adEntity.id) && Objects.equals(title, adEntity.title) && Objects.equals(target, adEntity.target) && Objects.equals(price, adEntity.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, target, price);
    }

    @Override
    public String toString() {
        return "AdEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + target + '\'' +
                ", price=" + price +
                '}';
    }
}
