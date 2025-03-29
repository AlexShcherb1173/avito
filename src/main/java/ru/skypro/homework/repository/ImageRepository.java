package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findById(int id);

    Optional<Image> findByFilePathContaining(String filepath);
}
