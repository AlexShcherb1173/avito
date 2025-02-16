package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.model.Advertisement;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByAuthorUsername(String username);

}
