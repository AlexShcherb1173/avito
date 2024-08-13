package ru.skypro.homework.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Comment {
    @Setter
    @Getter
    @Id
    private Long id;
    private Integer author;
    private String authorComment;
    private String authorFirstName;
    private Long editComment;
    private String text;

}