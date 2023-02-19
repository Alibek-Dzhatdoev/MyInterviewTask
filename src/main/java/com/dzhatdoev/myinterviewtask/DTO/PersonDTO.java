package com.dzhatdoev.myinterviewtask.DTO;

import com.dzhatdoev.myinterviewtask.models.Person;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PersonDTO {
    private static ModelMapper modelMapper = new ModelMapper();

    private int id;

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    private List<QuoteDTO> quoteDTOList;

    public static Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public static PersonDTO convertToDto(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public static List<PersonDTO> convertToDtoList (List<Person> personList) {
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person person : personList) personDTOList.add(convertToDto(person));
        return personDTOList;
    }
}