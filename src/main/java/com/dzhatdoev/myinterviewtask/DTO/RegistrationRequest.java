package com.dzhatdoev.myinterviewtask.DTO;

import com.dzhatdoev.myinterviewtask.models.Person;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;
    @Email(message = "Must be in email format")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @NotNull(message = "Password must not be null")
    private String password;

    public Person convertToPerson() {
        Person person = new Person();
        person.setEmail(email);
        person.setUsername(username);
        person.setPassword(password);
        return person;
    }

}
