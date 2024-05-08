package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(schema = "graduate", name = "users")
public class UserEntity implements UserDetails {
    /**
     * Id пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Логин пользователя / его email
     */
    @Column(name = "email", unique = true)
    private String username;

    /**
     * Пароль пользователя
     */
    @Column(name ="password", unique = true)
    private String password;

    /**
     * Имя пользователя
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Фамилия пользователя
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Телефон пользователя
     */
    @Column(name = "phone", nullable = false)
    private String phone;

    /**
     * Ссылка на аватар пользователя
     */
    @Column(name = "image")
    private String image;

    /**
     * Акитвация аккаунта
     * columnDefinition - значение по умолчанию
     */
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default false")
    private Boolean isActive;

    /**
     * Комментарии пользователя
     */
    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    /**
     * Объявления пользователя
     */
    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<AdEntity> ads;

    /**
     * Роль пользователя
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    /**
     * getAuthorities - полномочия
     * ofNullable - Роль может быть пустой
     * SimpleGrantedAuthority - полномочие по Spring Sc.
     * List::of - преобразовавыем в список полномочий
     * orElse(Collections.emptyList()) - если роль пустая, то создается пустой лист
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(role)
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .map(List::of)
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean isAccountNonExpired() { //Срок действий аккаунта **true - не истек срок
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //Аккаунт активен
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() { //Срок действия пароля
        return true;
    }

    @Override
    public boolean isEnabled() { //Аккаунт активен
        return isActive;
    }
}
