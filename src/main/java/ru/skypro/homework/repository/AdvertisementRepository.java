package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Advertisement;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement,Long> {
    List<Advertisement> findByAuthorId(Long authorId);  // Для получения объявлений пользователя
    Optional<Advertisement> findByIdAndAuthorId(Long id, Long authorId); // Для проверки владельца
}
