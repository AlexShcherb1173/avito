package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

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
        repository.deleteByUsername();
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
        Optional<UserEntity> user = Optional.empty();
        if (repository.findByUsername(username).equals(user)) {
            return UsernameNotFoundException;
        }
        return repository.findByUsername(username);
    }
}
