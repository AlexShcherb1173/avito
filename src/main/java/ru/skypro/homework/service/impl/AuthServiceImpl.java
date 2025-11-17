package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.MyUserDetailsManager;

/**
 * Service for login and register processes
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final MyUserDetailsManager myUserDetailsManager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthServiceImpl(MyUserDetailsManager myUserDetailsManager, PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
        this.myUserDetailsManager = myUserDetailsManager;
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    /**
     * Method for login
     *
     * @param loginDto with username and password
     */
    @Override
    public void login(LoginDto loginDto) {
        logger.info("Method for login was invoked");
        if (myUserDetailsManager.userExists(loginDto.getUsername())) {
            UserDetails userDetails = myUserDetailsManager.loadUserByUsername(loginDto.getUsername());
            if (encoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User {} successfully logged in", loginDto.getUsername());
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Method for registering the user *p*
     *
     * @param registerDto with register info
     * @return true if user is successfully registered
     */
    @Override
    public boolean register(RegisterDto registerDto) {
        logger.info("Method for registering was invoked");
        if (myUserDetailsManager.userExists(registerDto.getUsername())) {
            logger.info("User {} already exists", registerDto.getUsername());
            return false;
        }
        User user = userMapper.RegisterDtoToUserEntity(registerDto);
        String encodedPassword = encoder.encode(registerDto.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        logger.info("User {} was successfully registered", registerDto.getUsername());
        return true;
    }
}
