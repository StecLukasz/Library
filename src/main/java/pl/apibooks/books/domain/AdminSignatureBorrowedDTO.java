package pl.apibooks.books.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class AdminSignatureBorrowedDTO {

    private Long id;
    private String title;
    private String bookSignature;
    private String username;
    private String status;
    private Date borrowedDate;


}
