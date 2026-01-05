package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.DtoMapper;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;


    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(dtoMapper::toUser);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(dtoMapper::toUser);
    }

    public User registerUser(Register register) {
        UserEntity userEntity = dtoMapper.toUserEntity(register);
        userEntity.setEmail(register.getUsername()); // В Register поле username = email
        UserEntity savedEntity = userRepository.save(userEntity);
        return dtoMapper.toUser(savedEntity);
    }

    public Optional<User> updateUser(Integer id, UpdateUser updateUser, String image) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(updateUser.getFirstName());
                    existingUser.setLastName(updateUser.getLastName());
                    existingUser.setPhone(updateUser.getPhone());
                    existingUser.setImage(image);
                    UserEntity updated = userRepository.save(existingUser);
                    return dtoMapper.toUser(updated);
                });
    }

    public boolean updatePassword(Integer id, NewPassword newPassword) {
        return userRepository.findById(id)
                .map(user -> {
                    // Здесь должна быть проверка текущего пароля и хеширование нового
                    // Пока просто сохраняем как есть
                    user.setPassword(newPassword.getNewPassword());
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
