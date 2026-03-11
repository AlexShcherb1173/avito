package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
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

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    public User register(Register register) {
        UserEntity entity = userMapper.fromRegisterDto(register);
        UserEntity savedUser = userRepository.save(entity);
        return userMapper.toDto(savedUser);
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
}