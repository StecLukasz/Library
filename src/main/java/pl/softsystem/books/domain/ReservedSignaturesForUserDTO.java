package pl.softsystem.books.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class ReservedSignaturesForUserDTO {
    private String title;
    private String genre;
    private Set<Author> authors = new HashSet<>();
}
