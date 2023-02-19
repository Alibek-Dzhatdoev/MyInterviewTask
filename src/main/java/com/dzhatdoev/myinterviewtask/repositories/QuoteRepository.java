package com.dzhatdoev.myinterviewtask.repositories;

import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.models.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    List<Quote> findByAuthor (Person person);
}
