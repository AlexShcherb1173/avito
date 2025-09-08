package ru.skypro.homework.service.impl;

import com.sun.istack.logging.Logger;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.AuthService;
@Log4j2
@Service
public class AuthServiceImpl implements AuthService {

  private final UserServiceImpl manager;
  private final PasswordEncoder encoder;
  private final UserMapper userMapper;

  public AuthServiceImpl(UserServiceImpl manager, PasswordEncoder passwordEncoder, UserMapper userMapper) {
    this.manager = manager;
    this.encoder = passwordEncoder;
    this.userMapper = userMapper;
  }


  @Override
  @Transactional
  public boolean login(String userName, String password) {
    if (!manager.userExists(userName)) {
      return false;
    }
    UserDetails userDetails = manager.loadUserByUsername(userName);
    return encoder.matches(password, userDetails.getPassword());
  }

  @Override
  public boolean register(RegisterDto registerReq, Role role) {
    if (manager.userExists(registerReq.getUsername())) {
      return false;
    }
    registerReq.setRole(role);
    manager.createUser(userMapper.toUser(registerReq));
    log.info("Зарегистрирован новый пользователь: " );
    return true;
  }
}
