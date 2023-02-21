package com.dzhatdoev.myinterviewtask.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    @Column(unique = true)
    private String username;

    @Email(message = "Must be in email format")
    @NotEmpty(message = "Email must not be empty")
    @Column(unique = true)
    private String email;

    @Column
    @NotEmpty
    private String password;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column
    private String role;


    public Person(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return username.equals(person.username) && email.equals(person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    @Override
    public String toString() {
        return "id = " + id +
                ", username = " + username + '\'';
    }
}
