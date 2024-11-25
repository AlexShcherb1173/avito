package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Ad;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.model.AdEntity;

import javax.transaction.Transactional;
import java.util.List;

public interface AdRepository extends JpaRepository<AdEntity, Long> {

    List<Ad> findAllByUserUsername(String username);

    String findImageById(int id);

    AdEntity findAdEntityById(int id);

    AdEntity findAdEntityByImage(String filePathString);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ad_entity SET description=:description, price=:price, title=:title WHERE id=:id", nativeQuery = true)
    int updateInfoAboutAdById(int id, String description, Integer price, String title);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM ad_entity WHERE id = :id)", nativeQuery = true)
    boolean existsAdById(int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ad_entity SET image=:filePathString WHERE id=:id", nativeQuery = true)
    void saveNewAdImage(int id, String filePathString);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ad_entity WHERE id=:id", nativeQuery = true)
    void deleteAdById(int id);
}
