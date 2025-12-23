package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

public interface AdRepository extends JpaRepository<AdEntity, Integer> {
    List<AdEntity> findAllByAuthor_Id(Integer authorId);
}
