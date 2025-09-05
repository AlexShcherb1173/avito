package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public boolean login(String userName, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
            log.info("Login attempt for {}: {}", userName, matches ? "SUCCESS" : "FAILED");
            return matches;
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", userName);
            return false;
        }
    }

    @Override
    public boolean register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return false;
        }

        Users userEntity = UserMapper.INSTANCE.toEntity(register);
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
        userRepository.save(userEntity);

        return true;
    }


}





























//    @Service
//    public class AuthServiceImpl implements AuthService {
//
//        private final UserRepository userRepository;
//        private final PasswordEncoder passwordEncoder;
//        private final UserDetailsService userDetailsService;
//
//        public AuthServiceImpl(UserRepository userRepository,
//                               PasswordEncoder passwordEncoder,
//                               UserDetailsService userDetailsService) {
//            this.userRepository = userRepository;
//            this.passwordEncoder = passwordEncoder;
//            this.userDetailsService = userDetailsService;
//        }
//
//        @Override
//        public boolean login(String userName, String password) {
//            try {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//                return passwordEncoder.matches(password, userDetails.getPassword());
//            } catch (UsernameNotFoundException e) {
//                return false;
//            }
//        }
//        @Override
//    public boolean register(Register register) {
//        if (userRepository.existsByEmail(register.getUsername())) {
//            return false;
//        }
//
//        Users userEntity = UserMapper.INSTANCE.toEntity(register);
//        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
//        userRepository.save(userEntity);
//
//        return true;
//    }
//
//    }

//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final UserDetailsServiceImpl userDetailsService;
//
//    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.userDetailsService = userDetailsService;
//    }
//
//
//    @Override
//    public boolean login(String userName, String password) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//        return passwordEncoder.matches(password, userDetails.getPassword());
//    }
//
//    @Override
//    public boolean register(Register register) {
//        if (userRepository.existsByEmail(register.getUsername())) {
//            return false;
//        }
//
//        Users userEntity = UserMapper.INSTANCE.toEntity(register);
//        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
//        userRepository.save(userEntity);
//
//        return true;
//    }


//}
