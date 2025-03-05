package ru.skypro.homework.service.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.RoleDTO;
import ru.skypro.homework.model.User;
import ru.skypro.homework.security.CustomUserDetails;
import ru.skypro.homework.security.CustomUserDetailsManager;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.utility.SecurityUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomUserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final SecurityUtil securityUtil;

    public AuthServiceImpl(CustomUserDetailsManager manager,
                           PasswordEncoder passwordEncoder, SecurityUtil securityUtil) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.securityUtil = securityUtil;
    }

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password for user: " + userName);
        }
        securityUtil.setSetAuthentication(userDetails);
        return true;
    }

    @Override
    public boolean register(RegisterDTO register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }

        RoleDTO role = register.getRole();

        User user = new User()
                .setUsername(register.getUsername())
                .setPassword(register.getPassword())
                .setFirstName(register.getFirstName())
                .setLastName(register.getLastName())
                .setPhone(register.getPhone())
                .setRole(role)
                .setEnabled(true);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        securityUtil.setSetAuthentication(userDetails);
        manager.createUser(userDetails);

        return true;
    }
}
