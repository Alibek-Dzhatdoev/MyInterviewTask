package com.dzhatdoev.myinterviewtask.services;

import com.dzhatdoev.myinterviewtask.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final PeopleService peopleService;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public RegistrationService(PeopleService peopleService, BCryptPasswordEncoder passwordEncoder) {
        this.peopleService = peopleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        peopleService.checkDbIsExistsByNameThrown(person.getUsername());
        peopleService.checkDbIsExistsByEmailThrown(person.getEmail());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleService.save(person);
    }
}
