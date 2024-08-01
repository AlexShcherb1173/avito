package ru.skypro.homework.repository;
/*Репозиторий авторизация*/
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Authorities;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Authorities, Integer> {
    Optional<Authorities> findByUsername(String username);
    Optional<Authorities> save();


}
