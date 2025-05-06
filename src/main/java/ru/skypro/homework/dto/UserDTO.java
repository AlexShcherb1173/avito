package ru.skypro.homework.dto;

public class UserDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

    public UserDTO(String email, String firstName, String image, String lastName, String phone, Role role) {
        this.email = email;
        this.firstName = firstName;
        this.image = image;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }

    public UserDTO() {
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
}
