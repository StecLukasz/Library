package pl.softsystem.books.domain;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class SignatureDTO {
    private Long id;
    private Long bookId;
    private String title;
    private String bookSignature;
    private String username;
    private String status;
}
