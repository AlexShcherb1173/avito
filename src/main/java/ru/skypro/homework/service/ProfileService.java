package ru.skypro.homework.service;

import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;



public interface ProfileService {


    UserDto getProfile();


    UpdateUser updateProfile(UpdateUser updateUser);
}
