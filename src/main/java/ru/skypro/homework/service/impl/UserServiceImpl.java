package ru.skypro.homework.service.impl;


import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comment.UpdateUser;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.UserService;

public class UserServiceImpl {

    private final UsersRepository usersRepository;

    private final Users users = new Users(1,
            "user@gmail.com",
            "path-for-image",
            "user@gmail.com",
            "password",
            "ivan",
            "ivanov",
            "+7 777-77-77",
            Role.USER);

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
