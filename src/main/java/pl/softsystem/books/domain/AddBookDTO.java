package pl.softsystem.books.domain;

import java.util.HashSet;
import java.util.Set;

public class AddBookDTO {

    private String title;
    private String genre;
    private Set<Author> authors = new HashSet<>();
    private String status;
    private String signature;


}
