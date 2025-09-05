package ru.skypro.homework.service.impl;



import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import repository.AdRepository;
import repository.UserRepository;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.enity.Ad;
import ru.skypro.homework.enity.User;
import ru.skypro.homework.service.AdService;
import security.SecurityUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    // ===== Маппинг =====
    private AdDto map(Ad e) {
        AdDto dto = new AdDto();
        dto.setId(e.getId().intValue());
        dto.setTitle(e.getTitle());
        dto.setPrice(e.getPrice());
        dto.setImage(e.getImageUrl());
        return dto;
    }

    private ExtendedAd mapExt(Ad e) {
        ExtendedAd dto = new ExtendedAd();
        dto.setId(e.getId().intValue());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setPrice(e.getPrice());
        dto.setImage(e.getImageUrl());

        UserDto author = new UserDto();
        author.setId(e.getAuthor().getId().intValue());
        author.setFirstName(e.getAuthor().getFirstName());
        author.setLastName(e.getAuthor().getLastName());
        author.setPhone(e.getAuthor().getPhone());

        dto.setAuthor(author);
        return dto;
    }

    // ===== Реализация методов =====
    @Override
    public Ads getAll() {
        List<AdDto> list = adRepository.findAll().stream()
                .map(this::map)
                .collect(Collectors.toList());

        Ads wrapper = new Ads();
        wrapper.setCount(list.size());
        wrapper.setResults(list);
        return wrapper;
    }

    @Override
    public AdDto create(CreateOrUpdateAd dto, MultipartFile image) {
        User author = securityUtils.getCurrentUser();

        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setAuthor(author);

        // TODO: сохранить image и выставить URL
        ad.setImageUrl(null);

        ad = adRepository.save(ad);
        return map(ad);
    }

    @Override
    public ExtendedAd getById(Integer id) {
        Ad ad = adRepository.findById(id.longValue())
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
        return mapExt(ad);
    }

    @Override
    public AdDto update(Integer id, CreateOrUpdateAd dto) {
        Ad ad = adRepository.findById(id.longValue())
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
        checkOwnerOrAdmin(ad.getAuthor());

        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());

        return map(adRepository.save(ad));
    }

    @Override
    public void delete(Integer id) {
        Ad ad = adRepository.findById(id.longValue())
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
        checkOwnerOrAdmin(ad.getAuthor());
        adRepository.delete(ad);
    }

    @Override
    public Ads getMy() {
        User me = securityUtils.getCurrentUser();
        List<AdDto> list = adRepository.findByAuthorId(me.getId())
                .stream().map(this::map).collect(Collectors.toList());

        Ads wrapper = new Ads();
        wrapper.setCount(list.size());
        wrapper.setResults(list);
        return wrapper;
    }


    private void checkOwnerOrAdmin(User owner) {
        User current = securityUtils.getCurrentUser();
        if (!Objects.equals(current.getId(), owner.getId()) &&
                !current.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Forbidden");
        }
    }
}