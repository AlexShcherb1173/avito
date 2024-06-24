package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.UserAlreadyExistException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.*;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getMe() {
        logger.info("Invoked method getMe()");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUser(username);
    }

    @Override
    public User getUser(String email) {
        logger.info("Invoked method getUser({})", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User: " + email + " not found."));
    }

    @Override
    public void updateImage(String email, MultipartFile image) throws IOException, NoSuchElementException {
        logger.info("Invoked method updateImage for user: ({})", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User: " + email + " not found."));
        user.setImage(imageService.uploadImage(image));
        userRepository.save(user);
    }

    public User get(int id) throws NoSuchElementException {
        logger.info("Invoked method get(id), for user with id: ({})", id);
        return userRepository.findById(id).orElseThrow();
    }

    public void createUser(Register register) {
        logger.info("Invoked method createUser({})", register.getUsername());
        if (!userExists(register.getUsername())) {
            userRepository.save(userMapper.mapToUser(register));
        } else {
            logger.error("Register() throws UserAlreadyExistException");
            throw new UserAlreadyExistException("User " + register.getUsername() + " already exist");
        }
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        User user = getMe();
        logger.info("Invoked method updateUser(updateUser), for user: ({})", user.getEmail());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        userRepository.save(user);
        return userMapper.mapToUpdateUser(getUser(user.getEmail()));
    }

    @Override
    public void deleteUser(String username) {
        logger.info("Invoked method deleteUser({})", username);
        imageService.deleteImage(getUser(username).getImage().getId());
        userRepository.delete(getUser(username));
    }

    @Override
    public void changePassword(NewPassword password) {
        logger.info("Invoked method changePassword()");
        User userFromDb = getMe();
        if (encoder.matches(password.getCurrentPassword(), userFromDb.getPassword())) {
            userFromDb.setPassword(encoder.encode(password.getNewPassword()));
            userRepository.save(userFromDb);
        }
    }

    @Override
    public boolean userExists(String username) {
        logger.info("Invoked method userExists({})", username);
        return userRepository.existsByEmail(username);
    }
}
