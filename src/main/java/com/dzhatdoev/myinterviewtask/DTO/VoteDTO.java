package com.dzhatdoev.myinterviewtask.DTO;

import com.dzhatdoev.myinterviewtask.models.Vote;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

public class VoteDTO {
//    private int id;
//    private QuoteDTO quoteDTO;

    private static final ModelMapper mapper = new ModelMapper();
    private PersonDTO voterDTO;
    private boolean voteFor;
    private LocalDateTime voteDateTime;

    public static VoteDTO convertToDTO(Vote vote) {
        return mapper.map(vote, VoteDTO.class);
    }

}
