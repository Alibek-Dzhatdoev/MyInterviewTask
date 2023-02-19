package com.dzhatdoev.myinterviewtask.repositories;

import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.models.Quote;
import com.dzhatdoev.myinterviewtask.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Vote findByQuoteAndVoter (Quote quote, Person voter);

    List<Vote> findAllByQuote(Quote quote);
}
