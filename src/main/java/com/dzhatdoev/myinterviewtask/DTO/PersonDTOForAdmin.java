package com.dzhatdoev.myinterviewtask.DTO;

import com.dzhatdoev.myinterviewtask.models.Person;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PersonDTOForAdmin {
    private static ModelMapper modelMapper = new ModelMapper();

    private int id;

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;
    @Email(message = "Must be in email format")
    @NotEmpty(message = "Email must not be empty")
    private String email;


    private List<QuoteDTO> quoteDTOList;

    public static PersonDTOForAdmin convertToDto(Person person) {
        return modelMapper.map(person, PersonDTOForAdmin.class);
    }

    public static List<PersonDTOForAdmin> convertToAdminDtoList(List<Person> personList) {
        List<PersonDTOForAdmin> personDTOList = new ArrayList<>();
        for (Person person : personList) personDTOList.add(convertToDto(person));
        return personDTOList;
    }


}