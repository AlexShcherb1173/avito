package ru.skypro.homework.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;	        // id пользователя

    private String email;	    // логин пользователя

    private String password;    // пароль в зашифрованном виде

    @Column(name = "first_name")
    private String firstName;	// имя пользователя

    @Column(name = "last_name")
    private String lastName;	// фамилия пользователя

    private String phone;	    // телефон пользователя

    private String image;	    // ссылка на аватар пользователя

    private String role;        // роль пользователя

    @OneToMany(mappedBy = "author", cascade =  CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AdModel> adModels;       // объявления пользователя

    @OneToMany(mappedBy = "author", cascade =  CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CommentModel> commentModels; // Комментарии пользователя

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        GrantedAuthority authority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        };

        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
