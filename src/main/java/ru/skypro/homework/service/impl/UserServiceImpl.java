package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.generatedDto.UserDto;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.EmptyFileException;
import ru.skypro.homework.exception.UserHasNoRightsException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.exception.ImageNotFoundException;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, AvatarRepository avatarRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.avatarRepository = avatarRepository;
        this.userMapper = userMapper;
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDto getUserDtoByUsername(String username) {
        User user = getUser(username);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto getAuth() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principalUser = (UserDetails) currentUser.getPrincipal();
        return userMapper.userToUserDto(userRepository.findByUsername(principalUser.getUsername()).get());
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserDto currentUser = this.getAuth();
        User updatedUser = userRepository.findByUsername(currentUser.getEmail()).get();
        updatedUser.setFirstName(userDto.getFirstName());
        updatedUser.setLastName(userDto.getLastName());
        updatedUser.setPhone(userDto.getPhone());
        userRepository.save(updatedUser);
        return userMapper.userToUserDto(updatedUser);
    }

    @Override
    public void updateUserAvatar(String username, MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException();
        }

        User currentUser = getUser(username);

        Avatar avatar = avatarRepository.findByUserId(currentUser.getId()).orElse(new Avatar());

        avatar.setUser(currentUser);

        try {
            avatar.setImage(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Problems with uploaded image");
        }

        avatarRepository.save(avatar);
    }

    @Override
    public byte[] getUserAvatar(long id) {
        Avatar avatar = avatarRepository.findById(id).orElseThrow(ImageNotFoundException::new);
        return avatar.getImage();
    }

    @Override
    public void checkUserHasPermit(Authentication authentication, String username) {
        boolean matchUser = authentication.getName().equals(username);
        boolean userIsAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().contains(Role.ADMIN.name()));

        if (!(userIsAdmin || matchUser)){
            throw new UserHasNoRightsException("Current user has no rights to perform this operation.");
        }
    }
}
