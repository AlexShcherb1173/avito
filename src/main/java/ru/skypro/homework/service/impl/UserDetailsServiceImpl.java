package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Collections;

/**
 * Реализация {@link UserDetailsService} для Spring Security.
 * Загружает данные пользователя из базы данных для аутентификации.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает пользователя по имени пользователя (email).
     * Преобразует сущность {@link UserEntity} в объект Spring Security {@link UserDetails}.
     *
     * @param username email пользователя
     * @return объект {@link UserDetails} для Spring Security
     * @throws UsernameNotFoundException если пользователь с указанным email не найден
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                getAuthorities(userEntity)
        );
    }

    /**
     * Преобразует роль пользователя в коллекцию прав доступа Spring Security.
     *
     * @param userEntity сущность пользователя
     * @return коллекция прав доступа
     */
    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity userEntity) {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name())
        );
    }
}