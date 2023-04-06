package pl.softsystem.books.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@Data
@Getter
@Setter
public class AddBookDTO {

    private String title;
    private int pages;
    private String genre;
    private Set<AuthorDTO> authorsDTOS;
    private List<SignatureDTO> signaturesDTOS;
    private String status;


}
