package ru.skypro.homework.mapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setRole(user.getRole());
        if (user.getImage() != null) {
            userDTO.setImage("/image/" + user.getImage().getId());
        }
        return userDTO;
    }

    public UpdateUser mapToUpdateUser(User user) {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setPhone(user.getPhone());
        return updateUser;
    }

    public UserDetailsDTO mapToUserDetailsDTO(User user) {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(user.getRole().name().split(", "))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        UserDetailsDTO userDetails = new UserDetailsDTO();
        userDetails.setUsername(user.getEmail());
        userDetails.setPassword(user.getPassword());
        userDetails.setAuthorities(authorities);
        return userDetails;
    }

    public User mapToUser(Register register) {
        User user = new User();
        user.setEmail(register.getUsername());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPassword(register.getPassword());
        user.setPhone(register.getPhone());
        user.setRole(register.getRole());
        return user;
    }
}
