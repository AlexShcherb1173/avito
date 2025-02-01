package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.UserAvatar;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, Long>{
}
