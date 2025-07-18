package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean authenticate(String username, String password) {
        try {
            Optional<Users> user = usersRepository.findByEmail(username);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                return true;
            }
        } catch (UsernameNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean register(Register register) {
        if (usersRepository.existsByEmail(register.getEmail())) {
            return false; // Такой пользователь уже существует
        }

        Users user = new Users();
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(register.getRole());

        usersRepository.save(user);
        return true;
    }
}