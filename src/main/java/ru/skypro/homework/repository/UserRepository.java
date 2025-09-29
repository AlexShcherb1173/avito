package ru.skypro.homework.repository;


import org.springframework.stereotype.Repository;
import ru.skypro.homework.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}

