package ru.skypro.homework.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private LocalDateTime createdAt;
    private Integer pk;
    @Size(min = 8, max = 64)
    private String text;
}
