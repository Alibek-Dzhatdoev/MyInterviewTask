package com.dzhatdoev.myinterviewtask.services;

import com.dzhatdoev.myinterviewtask.DTO.PersonDTO;
import com.dzhatdoev.myinterviewtask.DTO.QuoteDTO;
import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.models.Quote;
import com.dzhatdoev.myinterviewtask.repositories.QuoteRepository;
import com.dzhatdoev.myinterviewtask.util.exceptions.QuoteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;

    private final PeopleService peopleService;


    public List<Quote> findAll() {
        List<Quote> list = quoteRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        Hibernate.initialize(list);
        return list;
    }

    public List<Quote> findAllSortedByVotes() {
        List<Quote> list = quoteRepository.findAll(Sort.by(Sort.Direction.DESC, "posOrNegVotes"));
        Hibernate.initialize(list);
        return list;
    }

    public List<QuoteDTO> findAllSortedByVotesConvertToDTO() {
        List<QuoteDTO> dtos = new ArrayList<>();
        List<Quote> quotes = findAllSortedByVotes();
        for (Quote quote : quotes) {
            QuoteDTO dto = QuoteDTO.convertToDTO(quote);
            dto.setAuthor(PersonDTO.convertToDto(quote.getAuthor()));
            dtos.add(dto);
        }
        return dtos;
    }

    public List<QuoteDTO> findAllConvertToDTO() {
        List<QuoteDTO> dtos = new ArrayList<>();
        List<Quote> quotes = findAll();
        for (Quote quote : quotes) {
            QuoteDTO dto = QuoteDTO.convertToDTO(quote);
            dto.setAuthor(PersonDTO.convertToDto(quote.getAuthor()));
            dtos.add(dto);
        }
        return dtos;
    }

    public Quote findByIdOrThrown(int id) {
        Optional<Quote> foundQuote = quoteRepository.findById(id);
        return foundQuote.orElseThrow(() -> new QuoteNotFoundException("Quote not found"));
    }

    public List<Quote> findByAuthorName(String authorName) {
        Person author = peopleService.findByUsernameOrThrown(authorName);
        return new ArrayList<>(quoteRepository.findByAuthor(author));
    }

    public List<Quote> findByAuthor(Person author) {
        return quoteRepository.findByAuthor(author);
    }

    public void deleteById(int id) {
        quoteRepository.deleteById(id);
    }

    @Transactional
    public void save(QuoteDTO quoteDTO) {
        Quote quote = QuoteDTO.convertToQuote(quoteDTO);
        quote.setAuthor(peopleService.getCurrentUser());
        quote.setCreatedAt(LocalDateTime.now());
        quote.setUpdatedAt(LocalDateTime.now());
        quoteRepository.save(quote);
    }

    @Transactional
    public void update(int id, QuoteDTO quoteDTO) {
        Optional<Quote> optQuote = quoteRepository.findById(id);
        Quote quote = optQuote.orElseThrow(() -> new QuoteNotFoundException("Quote with that id does not exists"));
        quote.setText(quoteDTO.getText());
        quote.setUpdatedAt(LocalDateTime.now());
        quoteRepository.save(quote);
    }

    public Quote getRandomQuote() {
        List<Quote> quoteList = quoteRepository.findAll();
        return quoteList.get((int) (Math.random() * quoteList.size()));
    }
}
