package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {
    List<AdEntity> findByAuthorId(Integer authorId);

    @Query("SELECT a FROM AdEntity a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<AdEntity> findByTitleContainingIgnoreCase(@Param("title") String title);
}