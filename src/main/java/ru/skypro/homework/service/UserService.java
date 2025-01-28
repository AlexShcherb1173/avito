package ru.skypro.homework.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.entity.RoleEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    Pattern pattern = Pattern.compile("\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}");
    UserMapper userMapper = new UserMapper();




    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }
    public boolean saveUser(Register user){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(user.getUsername());
        userEntity.setLastName(user.getLastName());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setPassword(encoder.encode(user.getPassword()));
        if(checkPhone(user.getPhone())){
            userEntity.setPhone(user.getPhone());
        }
        Role role = user.getRole();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setCaption(String.valueOf(role));
        userEntity.setRole(roleEntity);
        userRepository.save(userEntity);

        return true;
    }



    public boolean setPassword(String username, String password) {
        Optional<UserEntity> user = userRepository.findByUserName(username);
        if (user.isPresent()) {
            user.get().setPassword(password);
            userRepository.save(user.get());
        }else {
            throw new UsernameNotFoundException("User " + user + " not found");
        }
        return true;
    }

    public String getUser(Integer idUser){
        Optional<UserEntity> user = userRepository.findById(idUser);
        if (user.isPresent()) {
            return user.get().getUserName();
        }else {
            throw new UsernameNotFoundException("User id " + idUser + " not found");
        }

    }
    public boolean updateUser(UpdateUser user, Integer idUser) {
        Optional<UserEntity> updateUser = userRepository.findById(idUser);
        if (updateUser.isPresent() && checkPhone(user.getPhone())) {
            UserEntity userEntity = userMapper.updateUser(user, updateUser.get());
            userRepository.save(userEntity);
            return true;
        }else {
            throw new UsernameNotFoundException("User " + user + " not found");
        }
    }
    public void updateProfileImage(Integer userId, MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads", fileName);
            Files.write(path, file.getBytes());

            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            user.setProfilePicture(fileName); // Сохраняем имя файла в базу данных
            userRepository.save(user);

        }
    }
    private boolean checkPhone(String phone) {
        Matcher matcher = pattern.matcher(phone);
        if(matcher.matches()){
            return true;
        }
        throw new NoSuchElementException("Неверный номер телефона");

    }
}
