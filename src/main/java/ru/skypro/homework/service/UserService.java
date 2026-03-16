package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    public Optional<User> register(Register register) {
        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            return Optional.empty();
        }

        UserEntity entity = userMapper.fromRegisterDto(register);
        entity.setPassword(passwordEncoder.encode(register.getPassword()));

        UserEntity savedUser = userRepository.save(entity);
        return Optional.of(userMapper.toDto(savedUser));
    }

    public Optional<User> updateUser(Integer id, UpdateUser updateUser) {
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        UserEntity entity = userOptional.get();
        userMapper.updateUserFields(updateUser, entity);

        UserEntity updatedUser = userRepository.save(entity);
        return Optional.of(userMapper.toDto(updatedUser));
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User toDto(ru.skypro.homework.entity.UserEntity entity) {
        return userMapper.toDto(entity);
    }

}