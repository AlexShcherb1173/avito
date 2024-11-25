package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис для поиска пользователя по логину и оборачивания его в {@link UserDetailsImpl} <p>
 * Единственный метод сервиса {@link #loadUserByUsername}
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    /**
     * <b> Метод "загрузки" пользователя </b> <p>
     * Принцип работы:<p>
     * Ищет в БД пользователя по логину, если находит, оборачивает его в  {@link UserDetailsImpl} и возвращает
     * @param username логин
     * @return {@link UserDetailsImpl}
     * @throws UsernameNotFoundException (исключение если пользователь не найден)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не найден Пользователь"));

        return new UserDetailsImpl(user);
    }
}
