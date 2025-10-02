package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User mapToDto(ru.skypro.homework.model.User entity) {
        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole().name());
        return dto;
    }

    @Override
    public User getCurrentUser(Authentication authentication) {
        ru.skypro.homework.model.User entity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return mapToDto(entity);
    }

    @Override
    public void setPassword(Authentication authentication, String newPassword) {
        ru.skypro.homework.model.User entity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        entity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(entity);
    }

    @Override
    public UpdateUser updateUser(Authentication authentication, UpdateUser updateUser) {
        ru.skypro.homework.model.User entity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        entity.setFirstName(updateUser.getFirstName());
        entity.setLastName(updateUser.getLastName());
        entity.setPhone(updateUser.getPhone());

        userRepository.save(entity);

        return updateUser;
    }
}