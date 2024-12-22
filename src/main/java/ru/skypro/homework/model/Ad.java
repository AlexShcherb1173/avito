package ru.skypro.homework.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.Objects;
/**
 * Сущность для представления объявления.
 * <p>
 * В классе представлены данные об сущности объявления, а именно: автор, ссылка на картинку,
 * уникальный идентификатор, цена, заголовок и текст.
 * Также в классе реализованы конструкторы, геттеры, сеттеры, equals, hashCode и toString.
 * </p>
 */
@Entity
@Table(name = "ad")
@Transactional
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ad_id")
    private Integer id;

    private Integer price;

    private String title;

    private String adText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "image_url")
    private String imageUrl;

    public Ad() {}

    public Ad(Integer price, String title, String adText, User user) {
        this.price = price;
        this.title = title;
        this.adText = adText;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Ad{" + "pk=" + id + ", price=" + price + ", title='" + title + '\'' + ", adText='" + adText + '\'' + ", user=" + user + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return Objects.equals(id, ad.id) && Objects.equals(price, ad.price) && Objects.equals(title, ad.title) && Objects.equals(adText, ad.adText) && Objects.equals(user, ad.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, title, adText, user);
    }

    public Integer getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdText() {
        return adText;
    }

    public void setAdText(String adText) {
        this.adText = adText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
    }
}
