package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.autoAndReg.Register;
import ru.skypro.homework.mapper.RegisterMapper;
import ru.skypro.homework.service.AuthService;



@Service
@RequiredArgsConstructor
public interface AuthServiceImpl implements AuthService {
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final RegisterMapper registerMapper;
    private final UserService userService;

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }

        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }

        ru.skypro.homework.entity.User user = registerMapper.toEntity(register);

        user.setPassword(encoder.encode(user.getPassword()));

        userService.registerUser(user, register.getRole());

        return true;
    }

}
