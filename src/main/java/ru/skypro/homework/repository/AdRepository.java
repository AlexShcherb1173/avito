package ru.skypro.homework.repository;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.AdEntity;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {
    List<AdEntity> findByAuthorId(Integer authorId);
    void updateUserImage(byte[] image, Authentication authentication);
    void updateAdImage(Integer adId, byte[] image);
}
