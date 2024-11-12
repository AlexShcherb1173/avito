package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.AdModel;

public interface AdRepository extends JpaRepository<AdModel, Long> {
}
