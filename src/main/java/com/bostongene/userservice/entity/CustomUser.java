package com.bostongene.userservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import utills.LocalDateDeserializer;
import utills.LocalDateSerializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;


@Entity
public class CustomUser {

    @Id
    @GeneratedValue
    private Long id;


    private String firstName;

    private String lastName;

    private String password;

    @Column(unique = true)
    private String email;


    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    public CustomUser() {
    }

    public CustomUser(Long id, String firstName, String lastName, String password, String email, LocalDate date) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.date = date;
    }

    public CustomUser(String firstName, String lastName, String password, String email, LocalDate date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.date = date;
    }

    public CustomUser(String firstName, String lastName, String email, LocalDate date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomUser user = (CustomUser) o;

        if (!id.equals(user.id)) return false;
        if (!firstName.equals(user.firstName)) return false;
        if (!lastName.equals(user.lastName)) return false;
        if (!password.equals(user.password)) return false;
        if (!email.equals(user.email)) return false;
        return date.equals(user.date);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
