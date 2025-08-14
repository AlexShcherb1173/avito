package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Ad;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    @Query("SELECT a FROM Ad a JOIN FETCH a.author WHERE a.author.id = :authorId")
    List<Ad> findAllByAuthorId(@Param("authorId") Integer authorId);

    @Query("SELECT a FROM Ad a WHERE LOWER(a.title) LIKE LOWER(concat('%', :title, '%'))")
    List<Ad> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Override
    @Query("SELECT a FROM Ad a JOIN FETCH a.author LEFT JOIN FETCH a.image")
    List<Ad> findAll();
}