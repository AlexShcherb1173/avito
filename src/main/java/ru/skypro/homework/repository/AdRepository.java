package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {
    List<AdEntity> findAllByAuthor(UserEntity author);
    List<AdEntity> findAllByAuthor_Username(String username);

    @Query("SELECT a FROM AdEntity a WHERE a.title LIKE %:title%")
    List<AdEntity> findByTitleContaining(@Param("title") String title);
}