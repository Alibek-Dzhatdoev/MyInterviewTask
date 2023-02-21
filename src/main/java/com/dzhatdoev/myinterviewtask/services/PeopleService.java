package com.dzhatdoev.myinterviewtask.services;

import com.dzhatdoev.myinterviewtask.DTO.PersonDTO;
import com.dzhatdoev.myinterviewtask.DTO.PersonDTOForAdmin;
import com.dzhatdoev.myinterviewtask.DTO.QuoteDTO;
import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.repositories.PeopleRepository;
import com.dzhatdoev.myinterviewtask.util.exceptions.PersonNotCreatedException;
import com.dzhatdoev.myinterviewtask.util.exceptions.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final QuoteService quoteService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, @Lazy QuoteService quoteService) {
        this.peopleRepository = peopleRepository;
        this.quoteService = quoteService;
    }

    public List<Person> findAll() {
        Hibernate.initialize(peopleRepository.findAll());
        return peopleRepository.findAll();
    }

    public ResponseEntity<?> findByIdOrThrown(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        Person person = foundPerson.orElseThrow(() -> new PersonNotFoundException("Person with that ID not found"));
        Person currentUser = getCurrentUser();
        List<QuoteDTO> quoteDtoList = QuoteDTO.convertToDtoList(quoteService.findByAuthor(person));
        // Проверяем, является ли пользователь администратором или владельцем страницы
        // и возвращаем полную информацию
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

    public Person findByUsernameOrThrown(String name) {
        Optional<Person> foundPerson = peopleRepository.findByUsername(name);
        return foundPerson.orElseThrow(() -> new PersonNotFoundException("Person with that name not found"));
    }

    public void checkDbIsExistsByNameThrown(String username) {
        Optional<Person> person = peopleRepository.findByUsername(username);
        if (person.isPresent()) throw new PersonNotCreatedException("User with that username already exists");
    }

    public void checkDbIsExistsByEmailThrown(String email) {
        Optional<Person> person = peopleRepository.findByEmail(email);
        if (person.isPresent()) throw new PersonNotCreatedException("That is another person's email");
    }

    @Transactional()
    public void save(Person person) {
        person.setRole("ROLE_USER");
        person.setCreatedAt(LocalDateTime.now());
        peopleRepository.save(person);
    }

    @Transactional()
    public void update(PersonDTOForAdmin personToUpdate) {
        Person current = getCurrentUser();
        current.setUsername(personToUpdate.getUsername());
        current.setEmail(personToUpdate.getEmail());
        peopleRepository.save(current);
    }

    @Transactional
    public void assignAsAdmin(int id) {
        Person newAdmin = peopleRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person with that ID not found"));
        newAdmin.setRole("ROLE_ADMIN");
        peopleRepository.save(newAdmin);
    }

    @Transactional()
    public ResponseEntity<?> deleteById(int id) {
        Person user = peopleRepository.findById(id).orElseThrow(() -> new PersonNotFoundException("Person with that ID not found"));
        Person currentUser = getCurrentUser();
        // Проверяем, является ли пользователь администратором или владельцем страницы
        if (currentUser.getId() == user.getId() || currentUser.getRole().equals("ROLE_ADMIN")) {
            peopleRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Person removed");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this person");
        }
    }


    public Person getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails personDetails = (UserDetails) authentication.getPrincipal();
        return findByUsernameOrThrown(personDetails.getUsername());
    }


    public List<Person> findByNameStartingWith(String startingWith) {
        return peopleRepository.findByUsernameStartingWith(startingWith);
    }
}
