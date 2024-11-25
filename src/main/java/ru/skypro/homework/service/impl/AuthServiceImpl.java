package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetailsManager;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MyUserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);

        log.info("Информация о загруженном пользователе: {} | {}", userDetails.getUsername(), userDetails.getPassword());
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(register.getRole().name())
                        .build());
        /*
          Так как нельзя хранить пароли в явном виде,
          их нужно перед сохранением в БД хешировать,
          занимается этим метод register (мы в нём) {@link AuthServiceImpl#register(Register register)}
          и интерфейс PasswordEncoder получая при регистрации пароль от пользователя вызывается
          encoder.encode(<наш пароль>) (encoder экземпляр класса PasswordEncoder),
          который хеширует пароль и меняет его на хешированный в переданном объекте register.

          Далее объект register с хешированным паролем попадает в базу.

          При авторизации пользователь вводит свой пароль,
          полученный пароль хешируется в обратную сторону (строка 38)
          с помощью метода login {@link AuthServiceImpl#login(String username, String password)}
          и осуществляется проверка с введёными данными.
         */
        register.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(userMapper.toUserEntity(register));
        log.info("Информация о классе: {}", register.getClass());
        log.info("Пользователь создан. {}", manager.toString());
        return true;
    }

}
