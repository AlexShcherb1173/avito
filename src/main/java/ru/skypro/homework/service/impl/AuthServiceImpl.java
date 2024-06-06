package ru.skypro.homework.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.RegisterDtoMapperService;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder encoder;
    private final RegisterDtoMapperService registerDtoMapperService;
    private final UserRepository userRepository;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, RegisterDtoMapperService registerDtoMapperService, UserRepository userRepository) {
        this.encoder = passwordEncoder;
        this.registerDtoMapperService = registerDtoMapperService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean login(String userName, String password) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(userName);
        if (userEntityOptional.isEmpty()) {
            return false;
        }
        UserEntity userEntity = userEntityOptional.get();
        return encoder.matches(password, userEntity.getPassword());
    }

    @Override
    public boolean register(RegisterDto registerDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(registerDto.getUsername());
        if (userEntityOptional.isPresent()) {
            return false;
        }
        registerDto.setPassword(encoder.encode(registerDto.getPassword()));
        UserEntity userEntity = registerDtoMapperService.mappingToEntity(registerDto);
        userRepository.save(userEntity);
        return true;
    }

}
