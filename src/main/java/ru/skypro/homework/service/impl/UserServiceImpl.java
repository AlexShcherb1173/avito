package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Picture;
import ru.skypro.homework.entity.PictureOwner;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.MyUserDetailsManager;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

/**
 * Service for dealing with users (CRUD operations)
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MyUserDetailsManager myUserDetailsManager;
    private final PasswordEncoder encoder;
    private final PictureServiceImpl pictureServiceImpl;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, MyUserDetailsManager myUserDetailsManager, PasswordEncoder encoder, PictureServiceImpl pictureServiceImpl) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.myUserDetailsManager = myUserDetailsManager;
        this.encoder = encoder;
        this.pictureServiceImpl = pictureServiceImpl;
    }

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Method for setting the password
     *
     * @param newPasswordDto with old password to check and new password to update
     */
    @Override
    public void setPassword(NewPasswordDto newPasswordDto) {
        logger.info("Method for setting the password was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        if (!encoder.matches(newPasswordDto.getCurrentPassword(), myUserDetailsManager.getCurrentUser().getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        String encodedNewPassword = encoder.encode(newPasswordDto.getNewPassword());
        User user = myUserDetailsManager.getCurrentUser();
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        logger.info("New password was saved");
    }

    /**
     * Method for getting current user information
     *
     * @return returns UserDto
     */
    @Override
    @Transactional
    public UserDto getCurrentUserInformation() {
        logger.info("Method for getting current user information was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        return userMapper.UserToUserDto(myUserDetailsManager.getCurrentUser());
    }

    /**
     * Method for updating current user information
     *
     * @param updateUserDto with info to update
     * @return returns UpdateUserDto
     */
    @Override
    public UpdateUserDto updateUserInformation(UpdateUserDto updateUserDto) {
        logger.info("Method for updating current user information was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        User user = myUserDetailsManager.getCurrentUser();
        userMapper.safeUpdateUserDtoToUserEntity(updateUserDto, user);
        userRepository.save(user);
        logger.info("Updated user {}: firstName={}, lastName={}, phone={}", user.getEmail(), updateUserDto.getFirstName(), updateUserDto.getLastName(), updateUserDto.getPhone());
        return userMapper.UserToUpdateUserDto(user);
    }

    /**
     * Method for updating user picture *o.p*
     *
     * @param file file with picture
     * @throws IOException when uploading the picture
     */
    @Override
    public void uploadUserPicture(MultipartFile file) throws IOException {
        logger.info("Method for updating user picture was invoked");
        Picture picture = pictureServiceImpl.uploadPicture(myUserDetailsManager.getCurrentUser().getId(), PictureOwner.USER, file);
        User currentUser = myUserDetailsManager.getCurrentUser();
        currentUser.setImage(picture);
        userRepository.save(currentUser);
    }
}
