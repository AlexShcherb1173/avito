package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.UserModel;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<UserModel, Long> {
    UserModel findByEmail(String email);
}
