package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserDetailService;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            log.info("Пользователь не найден: {}", username);
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles()
                .accountLocked(false)
                .disabled(false)
                .build();
    }
}
