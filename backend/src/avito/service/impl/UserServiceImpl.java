package ru.avito.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.avito.dto.auth.NewPasswordRequest;
import ru.avito.dto.user.UpdateUserImageResponse;
import ru.avito.dto.user.UpdateUserRequest;
import ru.avito.dto.user.UserDto;
import ru.avito.entity.User;
import ru.avito.exception.BadRequestException;
import ru.avito.exception.NotFoundException;
import ru.avito.mapper.UserMapper;
import ru.avito.repository.UserRepository;
import ru.avito.security.SecurityUtils;
import ru.avito.service.ImageService;
import ru.avito.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    public UserDto getCurrentUser() {
        User user = getAuthenticatedUser();
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateCurrentUser(UpdateUserRequest request) {
        User user = getAuthenticatedUser();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void updatePassword(NewPasswordRequest request) {
        User user = getAuthenticatedUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UpdateUserImageResponse updateUserImage(MultipartFile image) {
        User user = getAuthenticatedUser();

        imageService.deleteImageIfExists(user.getImage());

        String imageUrl = imageService.saveUserImage(user.getId(), image);
        user.setImage(imageUrl);
        userRepository.save(user);

        return new UpdateUserImageResponse(imageUrl);
    }

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));
    }
}