package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Announcemenst {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int author;
    private String image;
    private int pk;
    private int price;
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_password")
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "announcements")
    private Collection<Comment> comments;


    public Announcemenst(int author, String image, int pk, int price,
                         String title, User user, Collection<Comment> comments) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
        this.user = user;
        this.comments = comments;
    }

    public Announcemenst() {
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }
}
