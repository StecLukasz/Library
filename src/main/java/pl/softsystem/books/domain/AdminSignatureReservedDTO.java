package pl.softsystem.books.domain;

import lombok.Data;

@Data
public class AdminSignatureReservedDTO {
    private Long id;
    private String bookSignature;
    private String username;
    private String status;
}
