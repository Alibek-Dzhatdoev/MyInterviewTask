package com.dzhatdoev.myinterviewtask.controllers;

import com.dzhatdoev.myinterviewtask.DTO.QuoteDTO;
import com.dzhatdoev.myinterviewtask.models.Person;
import com.dzhatdoev.myinterviewtask.models.Quote;
import com.dzhatdoev.myinterviewtask.models.Vote;
import com.dzhatdoev.myinterviewtask.services.PeopleService;
import com.dzhatdoev.myinterviewtask.services.QuoteService;
import com.dzhatdoev.myinterviewtask.services.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/quotes")
public class QuotesController {

    private final QuoteService quoteService;
    private final PeopleService peopleService;
    private final VoteService voteService;

    //Главная страница. Цитаты отсортированы по времени создания +++
    @ResponseBody
    @GetMapping
    public ResponseEntity<?> getAllLastQuotes() {
        return ResponseEntity.ok(quoteService.findAllConvertToDTO());
    }


    //Добавить цитату +++
    @ResponseBody
    @PostMapping
    public ResponseEntity<QuoteDTO> addQuote(@RequestBody @Valid QuoteDTO quoteDTO) {
        quoteService.save(quoteDTO);
        return new ResponseEntity<>(quoteDTO, HttpStatus.CREATED);
    }

    //Обновить цитату +++
    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateQuote(@PathVariable("id") int quoteId, @RequestBody @Valid QuoteDTO quoteDTO) {
        Person author = quoteService.findByIdOrThrown(quoteId).getAuthor();
        Person currentUser = peopleService.getCurrentUser();
        // Проверяем, является ли пользователь владельцем цитаты
        if (currentUser.getId() == author.getId()) {
            // Обновляем данные цитаты
            quoteService.update(quoteId, quoteDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Quote updated: " + quoteDTO.getText());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have no access to change this quote");
        }
    }

    //Удалить цитату ++
    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuote(@PathVariable int id) {
        Quote quote = quoteService.findByIdOrThrown(id);
        Person author = quote.getAuthor();
        Person currentUser = peopleService.getCurrentUser();

        if (currentUser.getId() == author.getId() || currentUser.getRole().equals("ROLE_ADMIN")) {
            quoteService.deleteById(quote.getId());
            return ResponseEntity.ok().body("Quote removed");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have no access to delete this quote");
        }
    }

    //Метод получения случайной цитаты   +++
    @ResponseBody
    @GetMapping("/random")
    public ResponseEntity<?> getRandomQuote() {
        QuoteDTO quoteDTO = QuoteDTO.convertToDTO(quoteService.getRandomQuote());
        return ResponseEntity.ok(quoteDTO);
    }


    //голосовать за цитату
    @PostMapping("/{id}")
    public ResponseEntity<?> voteForQuote(@PathVariable("id") int id,
                                          @RequestParam(name = "votes_for", required = true) Boolean voteFor) {
        Person voter = peopleService.getCurrentUser();
        Quote quote = quoteService.findByIdOrThrown(id);
        // Проверяем, что пользователь еще не голосовал за или против данной цитаты
        Vote existingVote = voteService.findByQuoteAndVoter(quote, voter);
        if (existingVote != null) {
            return ResponseEntity.badRequest().body("You already voted for this quote.");
        }
        // Создаем новый объект голоса и сохраняем его
        Vote vote = new Vote();
        vote.setQuote(quote);
        vote.setVoter(voter);
        vote.setVoteFor(voteFor);
        voteService.save(vote);

        // Обновляем количество голосов в соответствующем поле в объекте цитаты
        if (voteFor) {
            quote.incrementVotesFor();
        } else {
            quote.incrementVotesAgainst();
        }
        quoteService.update(quote.getId(), QuoteDTO.convertToDTO(quote));
        return ResponseEntity.ok(vote + " Vote counted successfully.");
    }

    //TODO просмотр 10 лучших и 10 худших цитат (в идеале график)

    @ResponseBody
    @GetMapping("/top")
    public ResponseEntity<?> getTop () {
        List<QuoteDTO> quoteDTOList = quoteService.findAllSortedByVotesConvertToDTO();
        return ResponseEntity.ok(quoteDTOList.subList(0, 10));
    }
    @ResponseBody
    @GetMapping("/flop")
    public ResponseEntity<?> getFlop () {
        List<QuoteDTO> quoteDTOList = quoteService.findAllSortedByVotesConvertToDTO();
        Collections.reverse(quoteDTOList);
        return ResponseEntity.ok(quoteDTOList.subList(0, 10));
    }


    //TODO посмотреть голоса цитаты
    @ResponseBody
    @GetMapping("/{id}/votes")
    public ResponseEntity<?> quotesVotes (@PathVariable("id") int id) {
        Quote quote = quoteService.findByIdOrThrown(id);
        List<Vote> votes = voteService.findAllByQuote(quote);
        return ResponseEntity.ok(votes);
    }

}

