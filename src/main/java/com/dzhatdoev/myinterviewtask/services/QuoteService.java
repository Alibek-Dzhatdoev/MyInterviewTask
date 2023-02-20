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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final QuoteService quoteService;


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

    public ResponseEntity<?> deleteById(int id) {
        Quote quote = quoteService.findByIdOrThrown(id);
        Person author = quote.getAuthor();
        Person currentUser = peopleService.getCurrentUser();
        if (currentUser.getId() == author.getId() || currentUser.getRole().equals("ROLE_ADMIN")) {
            quoteRepository.deleteById(id);
            return ResponseEntity.ok().body("Quote removed");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have no access to delete this quote");
        }
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
    public ResponseEntity<?> update(int id, QuoteDTO quoteDTO) {
        Person author = quoteService.findByIdOrThrown(id).getAuthor();
        Person currentUser = peopleService.getCurrentUser();
        // Проверяем, является ли пользователь владельцем цитаты
        if (currentUser.getId() == author.getId()) {
            // Обновляем данные цитаты
            Optional<Quote> optQuote = quoteRepository.findById(id);
            Quote quote = optQuote.orElseThrow(() -> new QuoteNotFoundException("Quote with that id does not exists"));
            quote.setText(quoteDTO.getText());
            quote.setUpdatedAt(LocalDateTime.now());
            quoteRepository.save(quote);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Quote updated: " + quoteDTO.getText());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have no access to change this quote");
        }
    }

    public Quote getRandomQuote() {
        List<Quote> quoteList = quoteRepository.findAll();
        return quoteList.get((int) (Math.random() * quoteList.size()));
    }
}
