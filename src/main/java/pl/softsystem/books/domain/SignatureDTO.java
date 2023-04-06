package pl.softsystem.books.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignatureDTO {
    private Long id;
    private String title;
    private String bookSignature;
    private String username;
    private String status;
}
