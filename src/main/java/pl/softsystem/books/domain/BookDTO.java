package pl.softsystem.books.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Data
@Getter
@Setter
public class BookDTO {

    private Long bookId;
    private String title;
    private int pages;
    private Date borrowedDate;
    private Date returnDate;
    private String status;
    private int availableQuantity;
    private String author;


}
