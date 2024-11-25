package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.model.AdEntity;

import java.util.List;

public interface AdRepository extends JpaRepository<AdEntity, Long> {

    List<Ad> findAllByUserUsername(String username);

    String findImageById(Integer id);

    AdEntity findAdEntityById(Integer id);

    AdEntity findAdEntityByImage(String filePathString);
}
