package com.dzhatdoev.myinterviewtask.controllers;

import com.dzhatdoev.myinterviewtask.DTO.QuoteDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/quotes")
public class QuotesController {

    private final QuoteService quoteService;
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
        return quoteService.update(quoteId, quoteDTO);
    }

    //Удалить цитату ++
    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuote(@PathVariable int id) {
        return quoteService.deleteById(id);
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
                                          @RequestParam(name = "votes_for") Boolean voteFor) {
        boolean voted = voteService.voteReturnVoted(id, voteFor);
        if (voted) {
            return ResponseEntity.badRequest().body("You already voted for this quote.");
        }
        return ResponseEntity.ok(" Vote counted successfully.");
    }

    //Просмотр 10 лучших и 10 худших цитат (в идеале график)
    @ResponseBody
    @GetMapping("/top")
    public ResponseEntity<?> getTop() {
        List<QuoteDTO> quoteDTOList = quoteService.findAllSortedByVotesConvertToDTO();
        return ResponseEntity.ok(quoteDTOList.subList(0, 10));
    }

    @ResponseBody
    @GetMapping("/flop")
    public ResponseEntity<?> getFlop() {
        List<QuoteDTO> quoteDTOList = quoteService.findAllSortedByVotesConvertToDTO();
        Collections.reverse(quoteDTOList);
        return ResponseEntity.ok(quoteDTOList.subList(0, 10));
    }


    //Посмотреть голоса цитаты
    @ResponseBody
    @GetMapping("/{id}/votes")
    public ResponseEntity<?> quotesVotes(@PathVariable("id") int id) {
        Quote quote = quoteService.findByIdOrThrown(id);
        List<Vote> votes = voteService.findAllByQuote(quote);
        return ResponseEntity.ok(votes);
    }

}

