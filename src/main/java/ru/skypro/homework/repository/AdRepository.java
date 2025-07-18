package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {
    List<Ad> findAllByAuthorId(Integer authorId);

    @Query("SELECT a FROM Ad a WHERE LOWER(a.title) LIKE LOWER(concat('%', :title, '%'))")
    List<Ad> findByTitleContainingIgnoreCase(String title);
}