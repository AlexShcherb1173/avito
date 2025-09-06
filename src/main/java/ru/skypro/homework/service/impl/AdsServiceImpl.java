package ru.skypro.homework.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.SecurityUtil;
import ru.skypro.homework.service.AdsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdsServiceImpl implements AdsService {

    private final AdRepository ads;
    private final UserRepository users;
    private final AdMapper mapper;
    private final SecurityUtil sec;

    public AdsServiceImpl(AdRepository ads, UserRepository users, AdMapper mapper, SecurityUtil sec) {
        this.ads = ads;
        this.users = users;
        this.mapper = mapper;
        this.sec = sec;
    }

    @Override
    @Transactional(readOnly = true)
    public Ads getAllAds() {
        List<Ad> list = ads.findAll().stream().map(mapper::toAdDto).collect(Collectors.toList());
        Ads res = new Ads();
        res.setCount(list.size());
        res.setResults(list);
        return res;
    }

    @Override
    public Ad addAd(CreateOrUpdateAd props, MultipartFile image) {
        UserEntity me = sec.currentUser();
        AdEntity e = mapper.fromCreateDto(props);
        e.setAuthor(me);
        AdEntity saved = ads.save(e);
        return mapper.toAdDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Ads getMyAds() {
        UserEntity me = sec.currentUser();
        List<Ad> list = ads.findAllByAuthor_Id(me.getId()).stream().map(mapper::toAdDto).collect(Collectors.toList());
        Ads res = new Ads();
        res.setCount(list.size());
        res.setResults(list);
        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getAd(int id) {
        AdEntity e = ads.findById(id).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        return mapper.toExtendedDto(e);
    }

    @Override
    public void removeAd(int id) {
        AdEntity e = ads.findById(id).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, e.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        ads.delete(e);
    }

    @Override
    public Ad updateAd(int id, CreateOrUpdateAd dto) {
        AdEntity e = ads.findById(id).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, e.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        mapper.updateEntity(dto, e);
        return mapper.toAdDto(e);
    }

    @Override
    public byte[] updateImage(int id, MultipartFile image) {
        AdEntity e = ads.findById(id).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, e.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        return new byte[0];
    }
}
