package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.enity.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.SecurityUtils;
import ru.skypro.homework.service.ProfileService;
import ru.skypro.homework.mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;

    @Override
    public UserDto getProfile() {
        User user = securityUtils.getCurrentUser();
        return UserMapper.toDto(user);
    }

    @Override
    public UpdateUser updateProfile(UpdateUser updateUser) {
        User user = securityUtils.getCurrentUser();
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        userRepository.save(user);
        return updateUser;
    }
}