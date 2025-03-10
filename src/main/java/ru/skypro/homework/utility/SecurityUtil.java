package ru.skypro.homework.utility;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Утилитный класс, предназначенный для работы с аутентификацией и текущим пользователем в контексте безопасности Spring Security.
 */
@Component
public class SecurityUtil {

    /**
     * Получает имя текущего авторизованного пользователя из контекста безопасности.
     * Если аутентификация не существует или не была установлена, метод возвращает null.
     *
     * @return Имя текущего пользователя, если аутентификация существует, или null в противном случае.
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) return null;

        return authentication.getName();
    }

    /**
     * Устанавливает аутентификацию для пользователя в контексте безопасности.
     * Использует переданный объект {@link UserDetails}, чтобы создать новую аутентификацию
     * и установить её в {@link SecurityContextHolder}.
     *
     * @param userDetails Объект {@link UserDetails}, представляющий пользователя, для которого нужно установить аутентификацию.
     */
    public void setSetAuthentication(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}