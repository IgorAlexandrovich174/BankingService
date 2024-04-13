package com.example.bankingservice.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "mails")
@Entity
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "mail")
    private String mail;
    @ManyToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail1 = (Mail) o;
        return Objects.equals(id, mail1.id) && Objects.equals(mail, mail1.mail) && Objects.equals(user, mail1.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mail, user);
    }

    @Override
    public String toString() {
        return "Mail{" +
               "id=" + id +
               ", mail='" + mail + '\'' +
               ", userId=" + user +
               '}';
    }
}
