package com.dzhatdoev.myinterviewtask.controllers;


import com.dzhatdoev.myinterviewtask.DTO.PersonDTO;
import com.dzhatdoev.myinterviewtask.DTO.PersonDTOForAdmin;
import com.dzhatdoev.myinterviewtask.DTO.QuoteDTO;
import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.repositories.PeopleRepository;
import com.dzhatdoev.myinterviewtask.services.PeopleService;
import com.dzhatdoev.myinterviewtask.services.QuoteService;
import com.dzhatdoev.myinterviewtask.services.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final PeopleService peopleService;
    private final QuoteService quoteService;
    private final PeopleRepository peopleRepository;


    //  Посмотреть список всех людей ++
    @ResponseBody
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(PersonDTOForAdmin.convertToAdminDtoList(peopleService.findAll()));
    }


    //Посмотреть профиль человека    ++
    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        Person person = peopleService.findByIdOrThrown(id);
        Person currentUser = peopleService.getCurrentUser();
        List<QuoteDTO> quoteDtoList = QuoteDTO.convertToDtoList(quoteService.findByAuthor(person));
        // Проверяем, является ли пользователь администратором или владельцем страницы
        if (currentUser.getRole().equals("ROLE_ADMIN") || currentUser.getUsername().equals(person.getUsername())) {
            PersonDTOForAdmin personDTOForAdmin = PersonDTOForAdmin.convertToDto(person);
            personDTOForAdmin.setQuoteDTOList(quoteDtoList);
            return ResponseEntity.ok(personDTOForAdmin);
        } else {
            // Возвращаем только id, имя пользователя и цитаты, скрывая остальную информацию
            PersonDTO personDTO = PersonDTO.convertToDto(person);
            personDTO.setQuoteDTOList(quoteDtoList);
            return ResponseEntity.ok(personDTO);
        }
    }

    //    Посмотреть свой профиль    ++
    @GetMapping("/my_page")
    public ResponseEntity<?> goToMyPage() {
        Person me = peopleService.getCurrentUser();
        List<QuoteDTO> quoteDtoList = QuoteDTO.convertToDtoList(quoteService.findByAuthor(me));
        PersonDTOForAdmin personDTOForAdmin = PersonDTOForAdmin.convertToDto(me);
        personDTOForAdmin.setQuoteDTOList(quoteDtoList);
        return ResponseEntity.ok(personDTOForAdmin);
    }

    // Метод для обновления информации о себе ++
    @ResponseBody
    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid PersonDTOForAdmin userToUpdate) {
        peopleService.update(userToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body("Your data updated");
    }

    // Метод для удаления пользователя ++
    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        Person user = peopleService.findByIdOrThrown(id);
        Person currentUser = peopleService.getCurrentUser();
        // Проверяем, является ли пользователь администратором или владельцем страницы
        if (currentUser.getId() == user.getId() || currentUser.getRole().equals("ROLE_ADMIN")) {
            peopleService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Person removed");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this person");
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponseEntity<?> makeAdmin(@PathVariable("id") int id) {
        peopleService.assignAsAdmin(id);
        return ResponseEntity.ok("Admin assigned");
    }
}
