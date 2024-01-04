package pl.apibooks.books.domain;

import lombok.Data;

import java.util.List;

@Data
public class BookRequestDTO {
    private String title;
    private int pages;
    private String genre;
    private List<String> signatures;
    private List<Long> authorIds;
}
