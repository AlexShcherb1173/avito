package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import java.util.List;

/**
 * Репозиторий для работы с комментариями (Comment).
 * Наследуется от {@link JpaRepository}, что позволяет использовать стандартные CRUD-методы.
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Метод для поиска всех комментариев, относящихся к указанному объявлению.
     *
     * @param ad Объявление, к которому относятся комментарии.
     * @return Список комментариев, связанных с указанным объявлением.
     */
    List<Comment> findByAd(Ad ad);
}
