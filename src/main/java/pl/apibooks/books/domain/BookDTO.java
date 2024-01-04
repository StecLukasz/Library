package pl.apibooks.books.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class BookDTO {

    private Long id;
    private Long bookId;
    private String title;
    private Date borrowedDate;
    private Date returnDate;
    private String status;

    private int pages;
    private String genre;
    private Set<AuthorDTO> authorDTO;
    private Set<AdminSignatureDTO> adminSignatureDTO;
    private List<BorrowedDTO> borrowedBookDTO;
}
