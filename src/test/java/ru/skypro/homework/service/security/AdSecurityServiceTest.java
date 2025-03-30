package ru.skypro.homework.service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdSecurityServiceTest {
    @Mock
    private AdRepository adRepository;

    @InjectMocks
    private AdSecurityService adSecurityService;

    private final User currentUsername = new User(1, "email", "pass", false,
            "Oleg", "Olegov", "+373777777", Role.USER, new Image(), null, null);
    private final Integer adId = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(currentUsername.getEmail());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testIsOwner_WhenAdExistsAndIsOwner() {

        Ad ad = new Ad();
        ad.setAuthor(currentUsername);

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

        boolean isOwner;
        if (adSecurityService.isOwner(adId)) isOwner = true;
        else isOwner = false;

        assertTrue(isOwner);
    }


    @Test
    void testIsOwner_WhenAdExistsAndIsNotOwner() {

        User anotherUser = new User(2, "another_email@example.com", "pass", false,
                "Maxim", "Anysimov", "+373777777", Role.USER, new Image(), null, null);
        Ad ad = new Ad();
        ad.setAuthor(anotherUser);

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

        boolean isOwner = adSecurityService.isOwner(adId);

        assertFalse(isOwner);
    }

    @Test
    void testIsOwner_WhenAdDoesNotExist() {

        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        assertThrows(AdNotFoundException.class, () -> adSecurityService.isOwner(adId));
    }
}