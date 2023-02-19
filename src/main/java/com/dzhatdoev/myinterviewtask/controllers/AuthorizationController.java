package com.dzhatdoev.myinterviewtask.controllers;

import com.dzhatdoev.myinterviewtask.DTO.PersonDTO;
import com.dzhatdoev.myinterviewtask.DTO.RegistrationRequest;
import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {

    private final RegistrationService registrationService;

    //Создание учетной записи юзера
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") RegistrationRequest registrationRequest) {
        return "registration";
    }

    @PostMapping(("/registration"))
    @ResponseBody
    public ResponseEntity<?> registration (@RequestBody @Valid RegistrationRequest request) {
        Person person = request.convertToPerson();
        registrationService.register(person);
        return new ResponseEntity<>(PersonDTO.convertToDto(person), HttpStatus.CREATED);
    }

    @PostMapping(("/registrationform"))
    @ResponseBody
    public String registrationForm (@ModelAttribute @Valid RegistrationRequest request) {
        Person person = request.convertToPerson();
        registrationService.register(person);
        return "redirect:/quotes";
    }
}





















