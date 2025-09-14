package ru.skypro.homework.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@Component
public class SecurityUtil {
    private final UserRepository users;
    public SecurityUtil(UserRepository users) { this.users = users; }

    public UserEntity currentUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !a.isAuthenticated()) return null;
        return users.findByEmail(a.getName()).orElse(null);
    }

    public boolean isAdmin(UserEntity u) { return u != null && u.getRole() == Role.ADMIN; }

    public boolean isOwner(UserEntity u, Integer authorId) {
        return u != null && authorId != null && u.getId().equals(authorId);
    }
}
