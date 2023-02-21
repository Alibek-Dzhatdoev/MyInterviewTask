package com.dzhatdoev.myinterviewtask.services;

import com.dzhatdoev.myinterviewtask.models.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PeopleService peopleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(Person person) {
        peopleService.checkDbIsExistsByNameThrown(person.getUsername());
        peopleService.checkDbIsExistsByEmailThrown(person.getEmail());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleService.save(person);
    }
}
