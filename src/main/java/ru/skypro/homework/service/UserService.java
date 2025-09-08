package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public User register(Register req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("User already exists: " + req.getUsername());
        }
        String encoded = passwordEncoder.encode(req.getPassword());
        UserEntity entity = userMapper.fromRegister(req, encoded);
        entity = userRepository.save(entity);
        return userMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public User getById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public User update(long id, UpdateUser req) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        userMapper.updateFrom(req, entity);
        return userMapper.toDto(entity);
    }
}
