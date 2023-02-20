package com.dzhatdoev.myinterviewtask.services;

import com.dzhatdoev.myinterviewtask.DTO.QuoteDTO;
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

    private final PeopleService peopleService;

    private final QuoteService quoteService;

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


    @Transactional
    public boolean voteReturnVoted (int id, Boolean voteFor) {
        boolean voted = false;
        Person voter = peopleService.getCurrentUser();
        Quote quote = quoteService.findByIdOrThrown(id);
        // Проверяем, что пользователь еще не голосовал за или против данной цитаты
        if (findByQuoteAndVoter(quote, voter) != null) {
            voted = true;
        }
        // Создаем новый объект голоса и сохраняем его
        Vote vote = new Vote();
        vote.setQuote(quote);
        vote.setVoter(voter);
        vote.setVoteFor(voteFor);
        save(vote);
        if (voteFor) {
            quote.incrementVotesFor();
        } else {
            quote.incrementVotesAgainst();
        }
        quoteService.update(quote.getId(), QuoteDTO.convertToDTO(quote));
        return voted;
    }
}
