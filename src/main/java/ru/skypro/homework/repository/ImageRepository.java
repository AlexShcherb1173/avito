package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
}
