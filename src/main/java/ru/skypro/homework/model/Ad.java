package ru.skypro.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User user;
    @Lob
    @Column(columnDefinition = "oid")
    @Basic(fetch = FetchType.LAZY)
    private byte [] image;
    private int price;
    private String title;
    private String description;
    @OneToMany(mappedBy = "ad")
    private List<Comment> commentList;



}
