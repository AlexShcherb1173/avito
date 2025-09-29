package ru.skypro.homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.enity.Ad;
import ru.skypro.homework.enity.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.SecurityUtils;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private AdServiceImpl adService;

    @Test
    void getMy_returnsAdsOfCurrentUser() {
        User user = new User();
        user.setId(1L);

        Ad ad1 = new Ad();
        ad1.setId(1L);
        ad1.setTitle("title1");
        ad1.setDescription("desc1");
        ad1.setPrice(100);
        ad1.setAuthor(user);
        ad1.setImageUrl("img1");

        Ad ad2 = new Ad();
        ad2.setId(2L);
        ad2.setTitle("title2");
        ad2.setDescription("desc2");
        ad2.setPrice(200);
        ad2.setAuthor(user);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(adRepository.findByAuthorId(user.getId())).thenReturn(List.of(ad1, ad2));

        Ads result = adService.getMy();

        assertEquals(2, result.getCount());
        assertEquals(2, result.getResults().size());

        AdDto dto1 = result.getResults().get(0);
        assertEquals(ad1.getId().intValue(), dto1.getId());
        assertEquals("title1", dto1.getTitle());
        assertEquals(100, dto1.getPrice());
        assertEquals("/ads/1/image", dto1.getImage());

        AdDto dto2 = result.getResults().get(1);
        assertEquals(ad2.getId().intValue(), dto2.getId());
        assertEquals("title2", dto2.getTitle());
        assertEquals(200, dto2.getPrice());
        assertNull(dto2.getImage());
    }
}