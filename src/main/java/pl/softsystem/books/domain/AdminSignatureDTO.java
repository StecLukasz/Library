package pl.softsystem.books.domain;

import lombok.Data;

@Data
public class AdminSignatureDTO {
    private Long id;
    private String title;
    private String bookSignature;
    private String username;
    private String status;
}

