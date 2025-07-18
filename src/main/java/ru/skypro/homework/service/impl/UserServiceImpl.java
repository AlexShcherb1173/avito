package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.comment.UpdateUser;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.AppMapper;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.UserService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final AppMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Users user = usersRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
        return mapper.userEntityToUserDTO(user);
    }

    @Override
    public User updateUserInfo(UpdateUser updateUser) {
        Users currentUser = usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        // Обновляем информацию пользователя
        currentUser.setFirstName(updateUser.getFirstName());
        currentUser.setLastName(updateUser.getLastName());
        currentUser.setPhone(updateUser.getPhone());

        return mapper.userEntityToUserDTO(usersRepository.save(currentUser));
    }

    @Override
    public void setUserImage(String imageURL) {
        Users currentUser = usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
        currentUser.setImage(imageURL);
        usersRepository.save(currentUser);
    }

    @Override
    public boolean registerUser(Register registerForm) {
        if (usersRepository.existsByEmail(registerForm.getEmail())) {
            return false; // Пользователь с таким email уже существует
        }

        Users newUser = new Users();
        newUser.setEmail(registerForm.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        newUser.setFirstName(registerForm.getFirstName());
        newUser.setLastName(registerForm.getLastName());
        newUser.setPhone(registerForm.getPhone());
        newUser.setRole(registerForm.getRole());

        usersRepository.save(newUser);
        return true;
    }
}