package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.exception.NotFoundException;

public interface UserDetailService {

    UserDetails loadUserByUsername(String username) throws NotFoundException;
}
