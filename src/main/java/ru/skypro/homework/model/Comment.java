package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private User author;
    private String authorImage;
    private String authorFirstName;
    private int createdAt;
    private int pk;
    private String text;
    @ManyToOne
    @JoinColumn(name = "announcements_author")
    @JsonIgnore
    private Announcemenst announcemenst;
    @ManyToOne
    @JoinColumn(name = "user_firstName")
    @JsonIgnore
    private User user;

    public Comment(User author, String authorFirstName, String authorImage, int createdAt, int pk,
                   String text, Announcemenst announcemenst, User user) {
        this.author = author;
        this.authorFirstName = authorFirstName;
        this.authorImage = authorImage;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
        this.announcemenst = announcemenst;
        this.user = user;
    }

    public Comment() {
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Announcemenst getAnnouncemenst() {
        return announcemenst;
    }

    public void setAnnouncemenst(Announcemenst announcemenst) {
        this.announcemenst = announcemenst;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
