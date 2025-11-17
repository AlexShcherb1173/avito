package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Picture;
import ru.skypro.homework.entity.PictureOwner;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.MyUserDetailsManager;

import java.io.IOException;
import java.util.List;

/**
 * Service for dealing with ads (CRUD operations)
 */
@Service
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final MyUserDetailsManager myUserDetailsManager;
    private final PasswordEncoder encoder;
    private final PictureServiceImpl pictureServiceImpl;

    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper, MyUserDetailsManager myUserDetailsManager, PasswordEncoder encoder, PictureServiceImpl pictureServiceImpl) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.myUserDetailsManager = myUserDetailsManager;
        this.encoder = encoder;
        this.pictureServiceImpl = pictureServiceImpl;
    }

    private final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    /**
     * Method for getting all the ads
     *
     * @return returns AdsDto with counter and list of AdDto
     */
    @Override
    @Transactional(readOnly = true)
    public AdsDto getAllAds() {
        logger.info("Method for getting all the ads was invoked");
        List<Ad> ads = adRepository.findAll();
        List<AdDto> allAds = adMapper.adListToAdDtoList(ads);
        AdsDto adsDto = new AdsDto();
        adsDto.setResults(allAds);
        adsDto.setCount(allAds.size());
        logger.info("Method for getting all the ads was finished");
        return adsDto;
    }

    /**
     * Method for adding an ad
     *
     * @param properties CreateOrUpdateAdDto with title, price and description to be set
     * @param image      file with an image
     * @return returns AdDto
     * @throws IOException in process of uploading the picture
     */
    @Override
    public AdDto addAd(CreateOrUpdateAdDto properties, MultipartFile image) throws IOException {
        logger.info("Method for adding an ad was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        Ad ad = adMapper.createOrUpdateAdDtoToAdEntity(properties);
        User author = myUserDetailsManager.getCurrentUser();
        ad.setUser(author);
        adRepository.save(ad);
        logger.info("Ad {} was added", ad.getId());
        Picture picture = pictureServiceImpl.uploadPicture(ad.getId(), PictureOwner.AD, image);
        ad.setImage(picture);
        logger.info("Picture for ad {} was added", ad.getId());
        adRepository.save(ad);
        logger.info("Method for adding an ad was finished");
        return adMapper.adToAdDto(ad);
    }

    /**
     * Method for getting an extended ad by id
     *
     * @param id ad id
     * @return returns ExtendedAdDto
     */
    @Override
    @Transactional(readOnly = true)
    public ExtendedAdDto getAdById(Integer id) {
        logger.info("Method for getting an extended ad by id was invoked");
        return adMapper.adtoExtendedAdDto(adRepository.findById(id).orElseThrow());
    }

    /**
     * Method for deleting ad by id
     *
     * @param id ad id
     */
    @Override
    public void removeAd(Integer id) {
        logger.info("Method for deleting ad by id was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        if (!findAdById(id).getUser().equals(myUserDetailsManager.getCurrentUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Ad ad = findAdById(id);
        ad.setImage(null);
        adRepository.save(ad);
        pictureServiceImpl.deleteByAdId(id);
        logger.info("Picture associated with ad {} was deleted", id);
        adRepository.deleteById(id);
        logger.info("Ad {} was deleted", id);
    }

    /**
     * Method for updating ad by id
     *
     * @param id                  ad id
     * @param createOrUpdateAdDto title, price and description to be updated
     * @return returns AdDto with updated information
     */
    @Override
    @Transactional
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto) {
        logger.info("Method for updating ad by id was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        if (!findAdById(id).getUser().equals(myUserDetailsManager.getCurrentUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Ad ad = findAdById(id);
        adMapper.updateAdFromCreateOrUpdateAdDto(createOrUpdateAdDto, ad);
        adRepository.save(ad);
        logger.info("Ad {} was updated", id);
        return adMapper.adToAdDto(ad);
    }

    /**
     * Method for getting all ads of current user
     *
     * @return returns AdsDto with counter and list of AdDto
     */
    @Override
    @Transactional(readOnly = true)
    public AdsDto getAllMyAds() {
        logger.info(" was invoked");
        myUserDetailsManager.checkUserAuthenticated();
        List<AdDto> myAdsDto = adMapper.adListToAdDtoList(adRepository.findAllMyAds(myUserDetailsManager.getCurrentUser().getId()));
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(myAdsDto.size());
        adsDto.setResults(myAdsDto);
        logger.info("All ads of current user were given");
        return adsDto;
    }

    /**
     * Method for updating ad picture
     *
     * @param id    ad id
     * @param image file with picture
     * @return returns picture
     * @throws IOException when uploading the picture
     */
    @Override
    public byte[] uploadAdPicture(Integer id, MultipartFile image) throws IOException {
        logger.info("Method for updating ad picture was invoked");
        Picture picture = pictureServiceImpl.uploadPicture(id, PictureOwner.AD, image);
        logger.info("Picture {} was uploaded", picture.getId());
        Ad currentAd = findAdById(id);
        currentAd.setImage(picture);
        adRepository.save(currentAd);
        logger.info("Picture {} was linked to the ad", picture.getId());
        return findAdById(id).getImage().getData();
    }

    /**
     * Sub-method for finding ad by id
     *
     * @param id ad id
     * @return returns Ad
     */
    @Override
    public Ad findAdById(Integer id) {
        logger.info("Sub-method for finding ad by id was invoked");
        return adRepository.findById(id).orElseThrow();
    }
}
