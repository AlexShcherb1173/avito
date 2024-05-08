package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.rq.user.NewPassword;
import ru.skypro.homework.dto.rq.user.UpdateUser;
import ru.skypro.homework.dto.rs.user.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.exception.WrongPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserMapper mapper, UserRepository userRepository, PasswordEncoder encoder) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void setPassword(NewPassword newPassword, String username) {
        UserEntity user = getUserByUsername(username);
        if (encoder.matches(user.getPassword(), newPassword.getCurrentPassword())) {
            user.setPassword(encoder.encode(newPassword.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new WrongPasswordException("Некорректные пароли");
        }
    }

    @Override
    public User getUser() {
        UserEntity userEntity = getUserByUsername(getCurrentUsername());
        return mapper.userEntityToUser(userEntity);

    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        UserEntity userEntity = getUserByUsername(getCurrentUsername());
        userEntity.setFirstName(updateUser.getFirstName());
        userEntity.setLastName(updateUser.getLastName());
        userEntity.setPhone(updateUser.getPhone());
        userRepository.save(userEntity);
        return mapper.entityToUpdateUser(userEntity);
    }

    private UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Такого пользователя не найдено"));
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
