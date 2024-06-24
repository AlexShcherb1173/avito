package ru.skypro.homework.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

@UtilityClass
public class UserValidate {
    public boolean isAdminOrOwner(Authentication authentication, String email) {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        boolean isOwner = authentication.getName().equals(email);
        return isAdmin || isOwner;
    }
}
