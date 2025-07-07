package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserModel;

@Component
public class UserMapper extends MapperAbstract {
    public User makeUser(UserModel userModel) {
        User user = new User();
        user.setImage(getFullImageUrl(userModel.getImage()));
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setPhone(userModel.getPhone());
        user.setRole(userModel.getRole());
        user.setId(userModel.getId());

        return user;
    }
}
