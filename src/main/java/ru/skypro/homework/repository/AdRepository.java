package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.model.Advertisement;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Advertisement, Integer> {
}
