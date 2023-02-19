package com.dzhatdoev.myinterviewtask.services;

import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.models.Quote;
import com.dzhatdoev.myinterviewtask.models.Vote;
import com.dzhatdoev.myinterviewtask.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    public Vote findByQuoteAndVoter(Quote quote, Person voter) {
        return voteRepository.findByQuoteAndVoter(quote, voter);
    }

    @Transactional
    public void save(Vote vote) {
        vote.setVoteDateTime(LocalDateTime.now());
        voteRepository.save(vote);
    }

    public List<Vote> findAllByQuote(Quote quote) {
        return voteRepository.findAllByQuote(quote);
    }


}
