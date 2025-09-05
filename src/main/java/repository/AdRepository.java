package repository;

import ru.skypro.homework.enity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByAuthorId(Long authorId);
}
