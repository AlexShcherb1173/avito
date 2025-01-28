package ru.skypro.homework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailServiceIml implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(UserDetailServiceIml.class);

    @Autowired
    private UserRepository userRepository;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByUserName(username);
        return userEntity.map(ru.skypro.homework.config.UserDetailsIml ::new)
                .orElseThrow(() -> new UsernameNotFoundException("%s not found".concat(username)));
    }
}
