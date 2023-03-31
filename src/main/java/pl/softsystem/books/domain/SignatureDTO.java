package pl.softsystem.books.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignatureDTO {
    private Long bookId;
    private String bookSignature;
}
