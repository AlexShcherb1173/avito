package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ad;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Archinski
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    @Transactional
    @Query(value = "SELECT * FROM ads WHERE user_id = ?1 ", nativeQuery = true)
    Optional<List<Ad>> findAllByUserId(@Param("userId") Integer userId);
}
