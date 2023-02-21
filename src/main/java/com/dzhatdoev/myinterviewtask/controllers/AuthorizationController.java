package com.dzhatdoev.myinterviewtask.controllers;

import com.dzhatdoev.myinterviewtask.DTO.PersonDTO;
import com.dzhatdoev.myinterviewtask.DTO.RegistrationRequest;
import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {

    private final RegistrationService registrationService;

    //Создание учетной записи юзера
    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping(("/registration"))
    @ResponseBody
    public ResponseEntity<?> registration (@Valid RegistrationRequest request) {
        Person person = request.convertToPerson();
        registrationService.register(person);
        return new ResponseEntity<>(PersonDTO.convertToDto(person), HttpStatus.CREATED);
    }

    @PostMapping(("/registrationform"))
    public String registrationForm (@ModelAttribute @Valid RegistrationRequest request) {
        Person person = request.convertToPerson();
        registrationService.register(person);
        return "redirect:/quotes";
    }
}





















