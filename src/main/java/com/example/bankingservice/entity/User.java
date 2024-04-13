package com.example.bankingservice.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "login")
    private String login;
    @Column(name = "password_hash")
    private String passwordHash;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<PhoneNumber> phoneNumbers;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Mail> mails;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAccountId() {
        return accountId;
    }
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
    public List<Mail> getMails() {
        return mails;
    }
    public void setMails(List<Mail> mails) {
        this.mails = mails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(accountId, user.accountId) && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(login, user.login) && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(phoneNumbers, user.phoneNumbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountId, dateOfBirth, login, passwordHash, phoneNumbers);
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", accountId=" + accountId +
               ", dateOfBirth=" + dateOfBirth +
               ", login='" + login + '\'' +
               ", passwordHash='" + passwordHash + '\'' +
               ", phoneNumbers=" + phoneNumbers +
               '}';
    }
}
