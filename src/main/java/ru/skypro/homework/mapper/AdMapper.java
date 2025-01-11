package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

@Component
public class AdMapper {

    private final UserRepository userRepository;

    public AdMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Преобразует сущность Ad в DTO.
     *
     * @param ad Сущность Ad.
     * @return Преобразованный объект AdDto.
     */
    public AdDto adToDto(Ad ad) {
        if (ad == null) {
            throw new IllegalArgumentException("Объявление не может быть null");
        }

        AdDto adDto = new AdDto();
        adDto.setId(ad.getPk());
        adDto.setTitle(ad.getTitle());
        adDto.setPrice(ad.getPrice());
        adDto.setImage(ad.getImage());
        adDto.setAuthor(ad.getUser() != null ? ad.getUser().getId() : null); // Убедимся, что автор не null
        return adDto;
    }

    /**
     * Преобразует DTO в сущность Ad.
     *
     * @param adDto DTO объявления.
     * @return Преобразованная сущность Ad.
     * @throws IllegalArgumentException Если DTO или автор не найдены.
     */
    public Ad dtoToAd(AdDto adDto) {
        if (adDto == null) {
            throw new IllegalArgumentException("DTO объявления не может быть null");
        }

        Ad ad = new Ad();
        ad.setPk(adDto.getId());
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setImage(adDto.getImage());

        // Находим пользователя по ID автора
        User user = userRepository.findById(adDto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + adDto.getAuthor() + " не найден"));
        ad.setUser(user);

        return ad;
    }
}