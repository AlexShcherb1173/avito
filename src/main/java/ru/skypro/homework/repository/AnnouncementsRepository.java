package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Announcemenst;

public interface AnnouncementsRepository extends JpaRepository<Announcemenst,Long> {
}
