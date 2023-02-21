package com.dzhatdoev.myinterviewtask.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class LoginRequest {
    private static ModelMapper modelMapper = new ModelMapper();
    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotEmpty
    private String password;
}
