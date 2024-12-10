package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

//@Table(name = "avatars")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Data
//@AllArgsConstructor
//@Entity
//public class AvatarEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    @Column(name = "file_size")
//    private long fileSize;
//    @Column(name = "media_type")
//    private String mediaType;
//    @Lob
//    @ToString.Exclude
//    private byte[] data;
//
//    @OneToOne
//    @JoinColumn(name="student_id")
//    private UserEntity user;
//    private String path;
//
//    public AvatarEntity() {
//
//    }
//}

@Table(name = "avatars")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@Entity
public class AvatarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String path;
    private long fileSize;
    private String mediaType;
    @JsonIgnore
    private byte[] data;
    @OneToOne
    @JsonIgnore
    private UserEntity user;

    public AvatarEntity() {

    }
}

