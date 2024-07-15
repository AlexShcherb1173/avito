package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.MappingControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.authentication.RegisterDto;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.exception.SuchAUserAlreadyExists;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.RegisterMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends UserNotFoundException implements UserService {
    private final AuthorityServiceImpl authorityService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final PasswordEncoder encoder;
    private final RegisterMapper registerMapper;

    @Override
    public UserDto getUserDto(String userName) {
        return userMapper.userToUserDto(getUser(userName));
    }

    @Override
    public User getUser(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public RegisterDto registerUser(RegisterDto body) throws SuchAUserAlreadyExists {
        User user = new User();
        user.setUsername(body.getUsername());
        user.setPassword(encoder.encode(body.getPassword()));
        user.setFirstName(body.getFirstName());
        user.setLastName(body.getLastName());
        user.setPhone(body.getPhone());
        user.setRole(body.getRole());
        user.setEnabled(true);
        if(userRepository.findAll().contains(user)) {
            throw new SuchAUserAlreadyExists();
        }
        userRepository.save(user);
        authorityService.addAuthorities(user);
        return registerMapper.userToUserDto(user);
    }

    @Override
    public byte[] getUserImage(int id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (user.getImage() == null) {
            throw new ImageNotFoundException();
        }

        return imageService.getImageData(user.getImage().getId());
    }

    @Override
    public UpdateUserDto updateUser(UpdateUserDto body, Authentication authentication) throws UserNotFoundException {
        User user = getUser(authentication.getName());
        User infoToUpdate = userMapper.updateUserDtoToUser(body);

        user.setFirstName(infoToUpdate.getFirstName());
        user.setLastName(infoToUpdate.getLastName());
        user.setPhone(infoToUpdate.getPhone());
        return userMapper.updateUserToUserDto(userRepository.save(user));
    }

    @Override
    public void updateNewPassword(NewPasswordDto body, Authentication authentication) throws UserNotFoundException {
        User user = getUser(authentication.getName());
        User infoToUpdate = userMapper.updateNewPasswordDtoToUser(body);

        user.setPassword(encoder.encode(infoToUpdate.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void uploadImage(MultipartFile imageMultipart, Authentication authentication) throws UserNotFoundException {
        User user = getUser(authentication.getName());

        Image image = imageService.addImage(imageMultipart);
        user.setImage(image);

        userRepository.save(user);
    }
}