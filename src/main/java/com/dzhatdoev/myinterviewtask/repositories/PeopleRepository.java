package com.dzhatdoev.myinterviewtask.repositories;

import com.dzhatdoev.myinterviewtask.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String name);

    List<Person> findByUsernameStartingWith(String startingWith);

    Optional<Person> findByEmail(String email);
}
