package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Authorities;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.LoginRepository;
import ru.skypro.homework.service.AuthorizationService;

@Service
@RequiredArgsConstructor
public interface AuthorizationServiceImpl implements AuthorizationService {
    private final LoginRepository loginRepository;

    @Override
    public void addAuthorities(User user, Role role) {
        Authorities authorities = new Authorities();
        authorities.setUsername(user.getUsername());
        authorities.setAuthority(role.name());

        LoginRepository.save(authorities);
    }
    @Override
    public String getAuthorities(User user) {
        Authorities authorities = new Authorities();
        authorities.setUsername(user.getUsername());
        authorities.setAuthority(LoginRepository.findByUsername(user.getUsername()).get().getAuthority());
        return authorities.getAuthority();
    }
}
