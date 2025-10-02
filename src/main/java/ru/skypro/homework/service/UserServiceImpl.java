package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserDto mapToDto(ru.skypro.homework.model.User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole().name());
        return dto;
    }

    @Override
    public UserDto getCurrentUser(Authentication authentication) {
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
    public UpdateUserDto updateUser(Authentication authentication, UpdateUserDto updateUser) {
        ru.skypro.homework.model.User entity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        entity.setFirstName(updateUser.getFirstName());
        entity.setLastName(updateUser.getLastName());
        entity.setPhone(updateUser.getPhone());

        userRepository.save(entity);

        return updateUser;
    }
}