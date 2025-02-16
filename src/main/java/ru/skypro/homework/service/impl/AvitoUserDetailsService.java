package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UserSecurityDTO;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

@Slf4j
@Service
public class AvitoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AvitoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь %s не зарегистрирован", username)));
        return new UserSecurityDTO(user);
    }
}