package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utility.SecurityUtil;

import java.io.IOException;

/**
 * Реализация интерфейса {@link UserService}, предоставляющая операции, связанные с пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final ImageServiceImpl imageServiceImpl;

    /**
     * Преобразует сущность {@link User} в {@link UserDTO}.
     *
     * @param user пользовательская сущность
     * @return соответствующий объект UserDTO
     */
    @Override
    public UserDTO getUserDTO(User user) {
        return userMapper.userToUserDTO(user);
    }

    /**
     * Преобразует {@link UserDTO} в сущность {@link User}.
     *
     * @param userDTO объект UserDTO
     * @return соответствующая сущность User
     */
    @Override
    public User createUserFromDTO(UserDTO userDTO) {
        return userMapper.userDTOToUser(userDTO);
    }

    /**
     * Обновляет информацию о текущем пользователе.
     *
     * @param updateUserDTO объект DTO с обновленными данными пользователя
     * @return обновленный UserDTO
     */
    @Override
    public UserDTO updateUser(UpdateUserDTO updateUserDTO) {
        User user = getCurrentUserEntity();
        user.setFirstName(updateUserDTO.getFirstName()).setLastName(updateUserDTO.getLastName()).setPhone(updateUserDTO.getPhone());
        userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    /**
     * Получает данные текущего аутентифицированного пользователя.
     *
     * @return объект UserDTO текущего пользователя
     */
    @Override
    public UserDTO getCurrentUser() {
        User user = getCurrentUserEntity();
        return userMapper.userToUserDTO(user);
    }

    /**
     * Получает сущность текущего аутентифицированного пользователя.
     *
     * @return сущность User текущего пользователя
     */
    @Override
    public User getCurrentUserEntity() {
        String username = securityUtil.getCurrentUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    /**
     * Обновляет пароль текущего пользователя после проверки текущего пароля.
     *
     * @param passwordDTO объект DTO с текущим и новым паролем
     */
    @Override
    public void updatePassword(NewPasswordDTO passwordDTO) {
        User currentUser = getCurrentUserEntity();
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), currentUser.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }
        currentUser.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(currentUser);
    }

    /**
     * Обновляет аватар пользователя.
     *
     * @param image новый файл изображения профиля
     * @return обновленный UserDTO
     */
    @Override
    public UserDTO updateUserImage(MultipartFile image) {
        try {
            User user = getCurrentUserEntity();
            String fileName = imageServiceImpl.saveImage(image);
            user.setImage(fileName);
            userRepository.save(user);
            return userMapper.userToUserDTO(user);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить изображение", e);
        }
    }
}
