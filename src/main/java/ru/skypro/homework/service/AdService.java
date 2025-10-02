package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean canEditAd(Integer adId, Authentication authentication) {
        Ad ad = adRepository.findById(adId).orElseThrow();
        User user = userRepository.findByEmail(authentication.getName());
        return ad.getAuthor().getId().equals(user.getId()) || user.getRole().equals(User.Role.ADMIN);
    }

    public Ad createAd(Ad ad, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        ad.setAuthor(user);
        return adRepository.save(ad);
    }
}
