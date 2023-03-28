package pl.softsystem.books.domain;

import lombok.Data;

@Data
public class reservedSignaturesForAdminDTO {
    private Long id;
    private String title;
    private String bookSignature;
    private String username;
    private String status;
}
