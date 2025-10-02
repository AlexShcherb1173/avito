package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.dto.UserDto;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhone(), user.getRole(), user.getImage());
    }

    public void updateUser(Integer id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        userRepository.save(user);
    }
}
