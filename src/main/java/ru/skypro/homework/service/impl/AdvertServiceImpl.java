package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdvertDto;
import ru.skypro.homework.dto.AdvertsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Advert;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdvertMapper;
import ru.skypro.homework.repositories.AdvertRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdvertService;
import ru.skypro.homework.service.ImageService;

import java.util.ArrayList;
import java.util.List;


@Service
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AdvertRepository advertRepository;

    @Override
    public AdvertsDto getAllAdverts() {
        AdvertsDto advertsDto = new AdvertsDto();
        List<AdvertDto> result = new ArrayList<>();
        advertRepository.findAll().forEach(u -> result.add(AdvertMapper.INSTANCE.adToAdDTO(u)));
        advertsDto.setResults(result);
        advertsDto.setCount(result.size());
        return advertsDto;
    }

    @Override
    public AdvertDto createAdvert(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile image) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        Advert advert = AdvertMapper.INSTANCE.createOrUpdateAdDTOToAd(createOrUpdateAdDto, user);
        advert.setImage(imageService.createImage(image));
        return AdvertMapper.INSTANCE.adToAdDTO(advertRepository.save(advert));
    }

    @Override
    public ExtendedAdDto getAdvertInfo(Long id) {
        Advert advert = advertRepository.findById(id).orElseThrow(RuntimeException::new);
        User user = userRepository.findById(advert.getAuthor().getId()).orElseThrow(RuntimeException::new);
        return AdvertMapper.INSTANCE.toExtendedAdDTO(advert, user);
    }

    @Override
    public Void deleteAdvert(Long id) {
        long imageId = advertRepository.findById(id).orElseThrow(RuntimeException::new).getImage().getId();
        advertRepository.deleteById(id);
        imageService.deleteImage(imageId);
        commentRepository.deleteAllByAdvertId(id);
        return null;
    }

    @Override
    public AdvertDto updateAdvert(Long id, CreateOrUpdateAdDto createOrUpdateAdDto) {
        Advert advert = advertRepository.findById(id).orElseThrow(RuntimeException::new);
        advert.setTitle(createOrUpdateAdDto.getTitle());
        advert.setPrice(createOrUpdateAdDto.getPrice());
        advert.setDescription(createOrUpdateAdDto.getDescription());
        return AdvertMapper.INSTANCE.adToAdDTO(advertRepository.save(advert));
    }

    @Override
    public AdvertsDto getAllAdvertsByAuthor() {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<AdvertDto> result = new ArrayList<>();
        advertRepository.findAllByAuthor(user).forEach(u -> result.add(AdvertMapper.INSTANCE.adToAdDTO(u)));
        AdvertsDto adsDTO = new AdvertsDto();
        adsDTO.setResults(result);
        adsDTO.setCount(result.size());
        return adsDTO;
    }

    @Override
    public Void updateAdvertImage(Long id, MultipartFile image) {
        Advert advert = advertRepository.findById(id).orElseThrow(RuntimeException::new);
        Long imageId = advert.getImage().getId();
        advert.setImage(imageService.createImage(image));
        imageService.deleteImage(imageId);
        advertRepository.save(advert);
        return null;
    }
}
