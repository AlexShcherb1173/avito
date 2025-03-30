package ru.skypro.homework.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.repository.UserRepository;

/**
 * Реализация сервиса загрузки пользователей для Spring Security.
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Конструктор UserDetailsServiceImpl.
     *
     * @param userRepository репозиторий пользователей
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по его email.
     *
     * @param username email пользователя
     * @return объект UserDetails
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username)
                .map(user -> {
                    log.debug("Преобразуем User в SecurityUser для пользователя {}", user.getEmail());
                    return new SecurityUser(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
