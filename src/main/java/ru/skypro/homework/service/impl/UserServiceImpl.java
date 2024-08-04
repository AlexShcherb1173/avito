package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapper.userDtoToUser(userDto);
        return mapper.userToUserDto(repository.save(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = repository.findById(id);
        return user.map(mapper::userToUserDto).orElse(null);
    }
}
