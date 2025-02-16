package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.dto.UserUpdateInfoDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.model.User;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.security.Principal;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper; // Внедряем маппер через Spring

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    @Override
    public void setPassword(NewPassword newPassword, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new WrongPasswordException(username);
        }
        try {
            user.setPassword(encoder.encode(newPassword.getNewPassword()));
            userRepository.save(user);
            log.info("Пароль изменен");
        } catch (WrongPasswordException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public UserDTO showUserInfo(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return userMapper.userToUserDto(user);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserUpdateInfoDTO userUpdateInfoDTO, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        user.setFirstName(userUpdateInfoDTO.getFirstName());
        user.setLastName(userUpdateInfoDTO.getLastName());
        user.setPhone(userUpdateInfoDTO.getPhone());

        userRepository.save(user);

        log.info("Изменена информация пользователя {}", principal.getName());
    }
}