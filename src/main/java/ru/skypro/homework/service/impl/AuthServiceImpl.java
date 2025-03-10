package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.check.CheckService;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.mapper.*;

import java.util.Optional;

@Slf4j
@Service
//@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;


    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;
    private final CheckService checkService;


    public AuthServiceImpl(
            PasswordEncoder passwordEncoder,
            UserDetailsManager manager,
            UserRepository userRepository,
            CheckService checkService,
            UserMapper userMapper) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.checkService = checkService;
        this.userMapper = userMapper;
    }

    @Override
    public boolean login(LoginDto loginDto) {
        log.info("The login method of AuthServiceImpl is called");

        if (!manager.userExists(loginDto.getUsername())) { // Проверяет, существует ли пользователь с указанным именем.
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(loginDto.getUsername()); // Получает информацию о пользователе по имени.

//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, loginDto.getPassword(), userDetails.getAuthorities());

        // Устанавливаем аутентификацию в контекст безопасности
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        return encoder.matches(loginDto.getPassword(), userDetails.getPassword()); // Сравнивает введенный пароль с хешированным паролем из базы данных.
    }


    @Override
    public boolean register(RegisterDto register) {
        log.info("The register method of AuthServiceImpl is called");

//        if (manager.userExists(register.getUsername())) {
//            return false;
//        }
//        manager.createUser(
//                User.builder()
//                        .passwordEncoder(this.encoder::encode)
//                        .password(register.getPassword())
//                        .username(register.getUsername())
//                        .roles(register.getRole().name())
//                        .build());


        Optional<UserEntity> user = userRepository.findByEmail(register.getUsername());
        if (user.isPresent()) {
            return false;
        }
        checkService.checkPhone(register.getPhone());
        UserEntity newUser = userMapper.toUser(register);
        newUser.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(newUser);
//        UserDetails userDetails = manager.loadUserByUsername(register.getUsername());
//        System.out.println();
        return true;
    }

//        UserDetails user = User.builder()
//                .username(register.getUsername())
//                .password(encoder.encode(register.getPassword()))
//                .roles(String.valueOf(register.getRole()))
//                .build();
//        userDetailsManager.createUser(user);
//        return true;
//    }


//    public void authenticateUser(String username, String password) {
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
//        Authentication authentication = authenticationManager.authenticate(token);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
}
