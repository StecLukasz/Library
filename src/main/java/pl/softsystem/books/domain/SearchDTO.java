package pl.softsystem.books.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class SearchDTO {
    private String title;
    private String genre;
    private Set<AuthorAdminDTO> authors = new HashSet<>();
    private long availableQuantity;
    private long signatureQuantity;
    private String bookStatusForUser;
}
