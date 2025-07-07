package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.Exception.UserAlreadyExistsException;
import ru.skypro.homework.dto.Registration.Register;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.Mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO login(String userName, String password) {
        UserEntity user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return userMapper.toDto(user);
    }

    @Override
    public UserDTO register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            throw new UserAlreadyExistsException("User already exists");
        }

        UserEntity newUser = userMapper.toEntity(register);
        newUser.setPassword(passwordEncoder.encode(register.getPassword()));
        UserEntity savedUser = userRepository.save(newUser);

        return userMapper.toDto(savedUser);
    }
}
