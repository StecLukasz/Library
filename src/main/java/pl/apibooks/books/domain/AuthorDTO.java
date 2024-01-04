package pl.apibooks.books.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class AuthorDTO {
    private Long Id;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthDate;
}
