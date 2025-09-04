package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.PasswordService;


import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @passwordServiceImpl.getUserEmailByAuthentication(#authentication)")
    @Transactional
    public boolean setPassword(NewPassword newPassword, Authentication authentication) {
        String username = authentication.name();
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
            return false;
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(userEntity);

        return true;
    }

    public String getUserEmailByAuthentication(Authentication authentication) {
        return authentication.name();
    }
}
