package ru.skypro.homework.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserDetailsManager {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, ImageRepository imageRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //загрузить пользователя по имени пользователя
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void createUser(UserEntity user) { //создать пользователя
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String getCurrentUsername() { //получить текущее имя пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public String getCurrentUserRole() { //получить текущую роль пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().iterator().next().getAuthority();
    }

    @Transactional
    public boolean setPassword(NewPasswordDto newPasswordDto ) {//установить пароль
        log.info("Внутри метода SetPassword");
        UserEntity currentUser = userRepository.findByUsername(getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (passwordEncoder.matches(newPasswordDto.getCurrentPassword(), currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public UserDto getUser() { //получить пользователя
        UserEntity currentUser = userRepository.findByUsername(getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDto userDto = new UserDto();
        log.info("Запрошенная информация: ");
        userMapper.toUserDto(userDto, currentUser);
        return userDto;
    }

    @Transactional
    public boolean updateUser(UserDto userDto) {
        Optional<UserEntity> currentUser = userRepository.findByUsername(getCurrentUsername());
        currentUser.ifPresent((user) -> userMapper.toUser(user, userDto));
        return currentUser.isPresent();
    }

    @Transactional(readOnly = true)
    public ImageEntity getUserImage(Integer userId) {
        return userRepository.findById(userId).map(UserEntity::getImage).orElse(null);
    }

    @Transactional
    public void updateUserImage(MultipartFile file) throws IOException {
        UserEntity user = userRepository.findByUsername(getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ImageEntity image = imageRepository.findById(user.getId()).orElse(new ImageEntity());
        image.setMediaType(file.getContentType());
        image.setData(file.getBytes());
        imageRepository.save(image);
        user.setImage(image);
    }


    @Override
    public void createUser(UserDetails user) {
    }

    @Override
    public void updateUser(UserDetails user) {
    }

    @Override
    public void deleteUser(String username) {
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
