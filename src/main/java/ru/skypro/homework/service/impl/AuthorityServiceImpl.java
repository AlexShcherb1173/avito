package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.Authority;
import ru.skypro.homework.entity.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AuthorityRepository;
import ru.skypro.homework.service.AuthorityService;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authoritiesRepository;

    @Override
    public void addAuthorities(User user) {
        Authority authority = new Authority();
        authority.setUsername(user.getUsername());
        //authority.setAuthority(role.name());

        authoritiesRepository.save(authority);
    }
    @Override
    public String getAuthorities(User user) {
        Authority authority = new Authority();
        authority.setUsername(user.getUsername());
        authority.setAuthority(authoritiesRepository.findByUsername(user.getUsername()).get().getAuthority());
        return authority.getAuthority();
    }
}