package ru.skypro.homework.entity;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@Table(name = "users")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String phone;

    private String password;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private ImageEntity image;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //получить полномочия
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() { //не истек ли срок действия аккаунта
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //аккаунт не заблокирован
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //Срок действия учетных данных не истек
        return true;
    }

    @Override
    public boolean isEnabled() { //включено
        return true;
    }
}
