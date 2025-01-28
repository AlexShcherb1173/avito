package ru.skypro.homework.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class RoleEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "caption")
    private String caption;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(caption, that.caption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, caption);
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                '}';
    }
}
