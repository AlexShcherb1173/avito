package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    @Query(value = "select * from ads\n" +
            "where id = (select comments.ad_id from comments\n" +
            "                                  where comments.id = ?1)",
            nativeQuery = true)
    AdEntity findByCommentId(int commentId);

    List<AdEntity> findAllByUser(UserEntity userEntity);
}
