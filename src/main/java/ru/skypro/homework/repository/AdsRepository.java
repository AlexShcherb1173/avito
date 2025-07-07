package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.AdModel;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<AdModel, Long> {
    @Query("FROM AdModel WHERE author.id = :authorId")
    List<AdModel> findByAuthorId(long authorId);
}
