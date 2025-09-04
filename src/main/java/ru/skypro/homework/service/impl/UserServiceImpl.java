package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public User getCurrentUser() {
        String username = getCurrentUsername();
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(userEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #userId == null or authentication.name == @userServiceImpl.getUserEmail(#userId)")
    @Transactional
    public User updateUser(UpdateUser updateUser, Integer userId) {
        String targetUsername = (userId != null) ? getUserEmail(userId) : getCurrentUsername();

        UserEntity userEntity = userRepository.findByEmail(targetUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateEntityFromDto(updateUser, userEntity);
        UserEntity savedEntity = userRepository.save(userEntity);

        return userMapper.toDto(savedEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userServiceImpl.getUserEmail(#userId)")
    public User getUserById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(userEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public String getUserEmail(Integer userId) {
        return userRepository.findById(userId).map(UserEntity::getEmail).orElseThrow(() -> new RuntimeException("User not found"));
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
