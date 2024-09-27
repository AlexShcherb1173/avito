package ru.skypro.homework.service.map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityNotFoundException;
@Component
public class CommentMap {
    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public CommentMap(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    /**
     * Метод преобразует Dto CreateOrUpdateCommentDto в объект класса AdComment.
     * @param createOrUpdateComment Dto {@link CreateOrUpdateCommentDto}.
     * @return объект класса Comment.
     */
    public Comment dtoToEntity(CreateOrUpdateCommentDto createOrUpdateComment, Integer adId, Integer userId){
        Comment comment = new Comment();
        comment.setText(createOrUpdateComment.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAd(adRepository.findById(adId).orElseThrow(() -> new EntityNotFoundException("Ad not found.")));
        comment.setUser(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found.")));
        return comment;
    }

    /**
     * Метод преобразует объект класса {@link Comment} в Dto {@link CommentDto}.
     * @param comment объект класса {@link Comment}.
     * @return Dto {@link CommentDto}.
     */
    public CommentDto toDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        User user = comment.getUser();

        commentDto.setAuthor(user.getId());
        commentDto.setAuthorImage("/users/" + user.getId() + "/image");
        commentDto.setAuthorFirstName(user.getFirstName());
        commentDto.setCreatedAt(comment.getCreatedAt().minusHours(3).toInstant(ZoneOffset.UTC).toEpochMilli());
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    /**
     * Метод преобразует список объектов класса {@link Comment} в Dto {@link CommentsDto}.
     * @param  list список объектов класса {@link Comment}.
     * @return Dto {@link CommentsDto}.
     */
    @Transactional
    public CommentsDto toComments(List<Comment> list) {
        int size = list.size();
        CommentsDto commentsDto = new CommentsDto();
        List<CommentDto> newList = new ArrayList<>();
        commentsDto.setCount(size);
        for (int i = 0; i < size; i++) {
            newList.add(toDto(list.get(i)));
        }
        commentsDto.setResults(newList);
        return commentsDto;
    }
}
