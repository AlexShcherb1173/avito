package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;
@Service
public class UserDetailsManagerImpl implements UserDetailsManager {
    private final UserRepository repository;

    public UserDetailsManagerImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createUser(UserDetails user) {
        repository.save((UserEntity)user);
    }

    @Override
    public void updateUser(UserDetails user) {
        repository.save((UserEntity)user);
    }

    @Override
    public void deleteUser(String username) {
        repository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = repository.findByUsername(username);
        if (user.isEmpty()) {
           throw new UsernameNotFoundException("Такого пользователя не найдено");
        }
        return user.get();
    }





}
