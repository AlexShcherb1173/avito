package ru.skypro.homework.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utility.SecurityUtil;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final ImageService imageService;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, SecurityUtil securityUtil, ImageService imageService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
        this.imageService = imageService;
    }

    @Override
    public UserDTO getUserDTO(User user) {
        return userMapper.userToUserDTO(user);
    }

    @Override
    public User createUserFromDTO(UserDTO userDTO) {
        return userMapper.userDTOToUser(userDTO);
    }

    @Override
    public UserDTO updateUser(UpdateUserDTO updateUserDTO) {
        User user = getCurrentUserEntity();
        user.setFirstName(updateUserDTO.getFirstName())
                .setLastName(updateUserDTO.getLastName())
                .setPhone(updateUserDTO.getPhone());
        userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO getCurrentUser() {
        User user = getCurrentUserEntity();
        return userMapper.userToUserDTO(user);
    }

    @Override
    public User getCurrentUserEntity() {
        String username = securityUtil.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public void updatePassword(NewPasswordDTO passwordDTO) {
        User currentUser = getCurrentUserEntity();
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), currentUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        currentUser.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(currentUser);
    }

    @Override
    public UserDTO updateUserImage(MultipartFile image) {
        try {
            User user = getCurrentUserEntity();
            String fileName = imageService.saveImage(image);
            user.setImage(fileName);
            userRepository.save(user);
            return userMapper.userToUserDTO(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
