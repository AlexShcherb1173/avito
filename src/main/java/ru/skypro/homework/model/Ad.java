package ru.skypro.homework.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @author Archinski
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String title;
    private String description;
    private Integer price;
    //    @Lob
//    @Column(columnDefinition = "oid")
    @Basic(fetch=FetchType.LAZY)
    private byte [] image;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;
}
