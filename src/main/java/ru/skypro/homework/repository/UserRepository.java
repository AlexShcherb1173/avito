package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.model.UserEntity;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    @Query(value = "SELECT password FROM app_user WHERE username=:username", nativeQuery = true)
    String findPasswordByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE app_user SET password=:newPassword WHERE username=:username", nativeQuery = true)
    int changePassword(String newPassword, String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE app_user SET first_name=:firstName, last_name=:lastName, phone=:phone WHERE username=:username",
            nativeQuery = true)
    void changeUserData(String firstName, String lastName, String phone, String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE app_user SET image =:filePath WHERE username=:username", nativeQuery = true)
    void saveAvatarPath(String filePath, String username);
}
