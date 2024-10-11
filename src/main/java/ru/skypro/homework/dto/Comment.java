package ru.skypro.homework.dto;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pk;
    @OneToOne
    @JoinColumn(name = "author")
    private int author;
    @OneToOne
    @JoinColumn(name = "authorImage")
    private String authorImage;
    @OneToOne
    @JoinColumn(name = "authorFirstName")
    private String authorFirstName;
    private int createdAt;
    private String text;
    // @OneToOne
    // @JoinColumn(name = "adId")
    // private int adId;

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
