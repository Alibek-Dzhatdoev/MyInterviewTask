package com.dzhatdoev.myinterviewtask.DTO;

import com.dzhatdoev.myinterviewtask.models.Quote;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class QuoteDTO {

    private static final ModelMapper mapper = new ModelMapper();

    private int id;
    private String text;
    private PersonDTO author;
    private int posOrNegVotes;


    public static Quote convertToQuote(QuoteDTO quoteDTO) {
        return mapper.map(quoteDTO, Quote.class);
    }


    public static QuoteDTO convertToDTO(Quote quote) {
        return mapper.map(quote, QuoteDTO.class);
    }

    public static List<QuoteDTO> convertToDtoList(List<Quote> quoteList) {
        List<QuoteDTO> quoteDTOList = new ArrayList<>();
        for (Quote quote : quoteList) quoteDTOList.add(convertToDTO(quote));
        return quoteDTOList;
    }


}
