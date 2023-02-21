package com.dzhatdoev.myinterviewtask.security;

import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PeopleService peopleService;

    @Autowired
    public PersonDetailsService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = peopleService.findByUsernameOrThrown(username);
        return new User(
                person.getUsername(),
                person.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(person.getRole()))
        );
    }
}
