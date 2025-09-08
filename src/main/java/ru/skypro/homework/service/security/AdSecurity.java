package ru.skypro.homework.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.skypro.homework.repository.AdRepository;

@Component("adSecurity")
@RequiredArgsConstructor
public class AdSecurity {

    private final AdRepository adRepository;

    public boolean isOwner(Long adId, Authentication authentication) {
        String username = authentication.getName();
        return adRepository.findById(adId)
                .map(ad -> ad.getAuthor() != null
                        && ad.getAuthor().getUsername().equals(username))
                .orElse(false);
    }
}
