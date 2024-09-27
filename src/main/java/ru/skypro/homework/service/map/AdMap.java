package ru.skypro.homework.service.map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdExtendedDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdMap {
    private final UserRepository userRepository;

    /**
     * Преобразует список объявлений
     *
     * @param adList список объектов класса
     * @return список объявлений
     */
    public AdsDto mapAdsDto(List<Ad> adList) {
        if (adList == null) {
            adList = Collections.EMPTY_LIST;
        } // для тестов
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(adList.size());
        adsDto.setResults(adList.stream().map(this::mapAdDto).collect(Collectors.toList()));
        return adsDto;
    }

    /**
     * Преобразует объект класса Ad
     *
     * @param ad объект класса
     * @return информация об объявлении
     */
    public AdDto mapAdDto(Ad ad) {
        if (ad == null) return null;
        AdDto adDto = new AdDto();
        adDto.setPk(ad.getId());
        adDto.setAuthor(ad.getUser().getId());
        adDto.setImage("/ads/" + ad.getId() + "/image");
        adDto.setPrice(ad.getPrice());
        adDto.setTitle(ad.getTitle());
        return adDto;
    }

    /**
     * Преобразует объект класса Ad с подробной информацией об авторе
     *
     * @param ad объект класса Ad
     * @return информация об объявлении в расширенном выходном формате
     */
    public AdExtendedDto mapAdExtendedDto(Ad ad) {
        AdExtendedDto adExtendedDto = new AdExtendedDto();
        adExtendedDto.setPk(ad.getId());
        adExtendedDto.setAuthorFirstName(ad.getUser().getFirstName());
        adExtendedDto.setAuthorLastName(ad.getUser().getLastName());
        adExtendedDto.setDescription(ad.getDescription());
        adExtendedDto.setEmail(ad.getUser().getUsername());
        adExtendedDto.setImage("/ads/" + ad.getId() + "/image");
        adExtendedDto.setPhone(ad.getUser().getPhone());
        adExtendedDto.setPrice(ad.getPrice());
        adExtendedDto.setTitle(ad.getTitle());
        return adExtendedDto;
    }

    /**
     * Преобразует информацию об объявлении в объект класса
     *
     * @param createOrUpdateAdDto информация об объявлении во входном формате
     * @param principal           принципал, чье имя используется для идентификации автора
     * @return объект класса
     */
    public Ad toEntity(CreateOrUpdateAdDto createOrUpdateAdDto, Principal principal) {
        Ad ad = new Ad();
        String userName = principal.getName();
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
        ad.setUser(user);
        ad.setTitle(createOrUpdateAdDto.getTitle());
        ad.setPrice(createOrUpdateAdDto.getPrice());
        ad.setDescription(createOrUpdateAdDto.getDescription());
        return ad;
    }
}
