package pl.softsystem.books.domain;

import lombok.Data;

import java.util.List;

@Data
public class AdminSignatureDTO {

    private String bookSignature;
    private List<Borrowed> borrowedBookList;



}

