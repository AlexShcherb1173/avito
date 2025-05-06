package ru.skypro.homework.model;

import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
    private String password;


    @OneToMany(mappedBy = "user")
    private Collection<Announcemenst> announcemensts;
    @OneToMany(mappedBy = "comment")
    private Collection<Comment> comment;

    public User(String email, String firstName, String image,
                String lastName, String phone, Role role,
                String password, Collection<Announcemenst> announcemensts, Collection<Comment> comment) {
        this.email = email;
        this.firstName = firstName;
        this.image = image;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.password = password;
        this.announcemensts = announcemensts;
        this.comment = comment;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Collection<Announcemenst> getAnnouncemensts() {
        return announcemensts;
    }

    public void setAnnouncemensts(Collection<Announcemenst> announcemensts) {
        this.announcemensts = announcemensts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Comment> getComment() {
        return comment;
    }

    public void setComment(Collection<Comment> comment) {
        this.comment = comment;
    }
}
