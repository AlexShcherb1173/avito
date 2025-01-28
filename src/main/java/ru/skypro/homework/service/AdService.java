package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AdService {

    private final AdRepository adRepository;
    private Logger logger = LoggerFactory.getLogger(AdService.class);
    AdMapper adMapper = new AdMapper();
    private final UserRepository repositoryUser;



    public AdService(AdRepository adRepository, UserRepository repository) {
        this.adRepository = adRepository;
        this.repositoryUser = repository;
    }

    public Ads getAllAd(){
        List<AdEntity> allAdEntity = adRepository.findAll();

        Ads allAd = adMapper.getAllAd(allAdEntity);
        return allAd;


    }
    public boolean addAd(Ad ad, MultipartFile image){
        try {
            if(ad != null && image != null) {
                AdEntity adEntity = adMapper.addAd(ad, image);
                Optional<UserEntity> user = repositoryUser.findById(ad.getAuthor());
                if(user.isPresent()) {
                    adEntity.setUser(user.get());
                    adRepository.save(adEntity);
                    return true;
                }

            }
        }catch (IOException e){
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e);

        }
        return false;
    }
    public ExtendedAd getAdById(Integer id){
        Optional<AdEntity> ad = adRepository.findById(id);
        if(ad.isPresent()) {
            ExtendedAd extendedAd = adMapper.getExtendedAd(ad.get());
            return extendedAd;
        }
        throw new NoSuchElementException();
    }

    public AdEntity removeAd(Integer id){
        Optional<AdEntity> removeAd = adRepository.findById(id);
        if(removeAd.isPresent()) {
            adRepository.delete(removeAd.get());
            return removeAd.get();
        }
        throw new NoSuchElementException();
    }
    public CreateOrUpdateAd updateAd(CreateOrUpdateAd ad, Integer id){
        Optional<AdEntity> adExisting = adRepository.findById(id);
        if(adExisting.isPresent()) {
            AdEntity adEntity = adMapper.updateAd(adExisting.get(), ad);
            adRepository.save(adEntity);
            return ad;
        }
        throw new NoSuchElementException();
    }

    public void updateImageAd(Integer id, MultipartFile image){
        AdEntity ad = adRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Объявление не найдено"));
        try {
            if (image != null && !image.isEmpty()) {
                String fileName = Instant.now().toString() + "-" + image.getOriginalFilename();
                FileOutputStream outputStream = new FileOutputStream("images/" + fileName);
                outputStream.write(image.getBytes());
                outputStream.close();

                ad.setImageFileName(fileName);
                adRepository.save(ad);
            }
        }catch (IOException e){
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }


    }



}
