package pl.softsystem.books.domain;

import lombok.Data;

@Data
public class ReservedSignaturesForAdminDTO {
    private Long id;
    private String title;
    private String bookSignature;
    private String username;
    private String status;
}
