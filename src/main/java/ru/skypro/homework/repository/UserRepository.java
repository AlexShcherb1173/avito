package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    boolean existsByUsername(String username);

    UserModel findByUsername(String username);
}
