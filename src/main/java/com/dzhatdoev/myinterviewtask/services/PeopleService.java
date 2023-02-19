package com.dzhatdoev.myinterviewtask.services;

import com.dzhatdoev.myinterviewtask.DTO.PersonDTOForAdmin;
import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.repositories.PeopleRepository;
import com.dzhatdoev.myinterviewtask.security.PersonDetails;
import com.dzhatdoev.myinterviewtask.util.exceptions.PersonNotCreatedException;
import com.dzhatdoev.myinterviewtask.util.exceptions.PersonNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PeopleService {

    private final PeopleRepository peopleRepository;

    public List<Person> findAll() {
        Hibernate.initialize(peopleRepository.findAll());
        return peopleRepository.findAll();
    }

    public Person findByIdOrThrown(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElseThrow(() -> new PersonNotFoundException("Person with that ID not found"));
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
        Person newAdmin = findByIdOrThrown(id);
        newAdmin.setRole("ROLE_ADMIN");
        peopleRepository.save(newAdmin);
    }

    @Transactional()
    public void deleteById(int id) {
        peopleRepository.deleteById(id);
    }


    public Person getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return findByUsernameOrThrown(personDetails.getUsername());
    }


    public List<Person> findByNameStartingWith(String startingWith) {
        return peopleRepository.findByUsernameStartingWith(startingWith);
    }
}
