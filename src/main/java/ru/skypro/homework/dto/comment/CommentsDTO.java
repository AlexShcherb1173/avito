package ru.skypro.homework.dto.comment;

import lombok.*;

import java.util.List;

/**
 * <b>DTO (Data Transfer Object) класс для представления комментариев. </b> <p>
 * <p>
 * Этот класс используется для передачи информации о комментариях, включая общее количество комментариев
 * и список самих комментариев.
 * </p>
 */
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO {

    /**
     * <b>Общее количество комментариев. </b> <p>
     */
    private int count;

    /**
     * <b>Список комментариев, представленных в виде объектов {@link CommentDTO}. </b> <p>
     */
    private List<CommentDTO> results;

}
