package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    List<AdEntity> findByAuthorId(Integer authorId);

    Optional<AdEntity> findById(Integer id);

    @Query("SELECT a FROM AdEntity a WHERE a.author.id = :userId")
    List<AdEntity> findAllByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(a) FROM AdEntity a WHERE a.author.id = :userId")
    Integer countByUserId(@Param("userId") Integer userId);

    @Query("SELECT a FROM AdEntity a WHERE a.author.email = :username")
    List<AdEntity> findByAuthorEmail(@Param("username") String username);
}