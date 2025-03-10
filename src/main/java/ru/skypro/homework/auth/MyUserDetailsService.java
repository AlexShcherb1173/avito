package ru.skypro.homework.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.dto.RoleDto;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsManager {
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;


    public MyUserDetailsService(PasswordEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.info("Имя пользователя не найдено: " + email);
//            return new UsernameNotFoundException(email);
            System.out.println("exception");
            return null;
        });
        return new MyUserPrincipal(user);
    }

    @Override
    public void createUser(UserDetails user) {
        UserEntity user1 = new UserEntity();
        user1.setPassword(user.getPassword());
        user1.setEmail(user.getUsername());
        user1.setRole(RoleDto.valueOf(
                user.getAuthorities()
                        .stream()
                        .findFirst()
                        .orElseThrow()
                        .getAuthority()
                        .replace("ROLE_", "")));
        userRepository.save(user1);
        log.info("Вы успешно добавили нового пользователя");
    }

    @Override
    public void updateUser(UserDetails user) {
        UserEntity userEdit = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> {
//            log.info("Пользователь не найден", UserNotFoundException.class);
//            return new UserNotFoundException();
            return null;
        });
        userEdit.setEmail(user.getUsername());
        userRepository.save(userEdit);
//        logger.info("Вы успешно обновили пользователя");
    }

    @Override
    public void deleteUser(String username) {
        UserEntity userToDelete = userRepository.findByEmail(username).orElseThrow(() -> {
//            log.info("Пользователь не найден", UserNotFoundException.class);
//            return new UserNotFoundException();
            return null;
        });
        userRepository.delete(userToDelete);
//        logger.info("Вы успешно удалили пользователя " + userToDelete);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTH " + auth.getName());
        UserEntity user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> {

            log.info("User not found", UserNotFoundException.class);
            return new UserNotFoundException("User not found exception");
        });

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(newPassword)
                        .username(user.getEmail())
                        .roles(user.getRole().name())
                        .build();

        user.setPassword(userDetails.getPassword());
        userRepository.save(user);
//        logger.info("Вы успешно изменили пароль");
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByEmail(username).isPresent();
    }
}
