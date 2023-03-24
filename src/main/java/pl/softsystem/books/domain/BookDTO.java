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
    private Date borrowedDate;
    private Date returnDate;
    private String status;


    public BookDTO(Long bookId, String title, Date borrowedDate, Date returnDate, String status) {

    }

    public BookDTO() {

    }
}
