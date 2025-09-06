package ru.skypro.homework.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #userId == null or authentication.name == @userServiceImpl.getUserEmail(#userId)")
    @Transactional
    public User updateUser(UpdateUser updateUser, Integer userId) {
        String targetUsername = (userId != null) ? getUserEmail(userId) : getCurrentUsername();

        Users userEntity = userRepository.findByEmail(targetUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserMapper.INSTANCE.updateEntityFromDto(updateUser, userEntity);
        Users savedEntity = userRepository.save(userEntity);

        return UserMapper.INSTANCE.toDto(savedEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userServiceImpl.getUserEmail(#userId)")
    public User getUserById(Integer userId) {
        Users userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.INSTANCE.toDto(userEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public String getUserEmail(Integer userId) {
        return userRepository.findById(userId).map(Users::getEmail).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

}
