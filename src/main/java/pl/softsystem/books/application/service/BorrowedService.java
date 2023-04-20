package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.Borrowed;
import pl.softsystem.books.domain.BookRepository;
import pl.softsystem.books.domain.Borrowed;
import pl.softsystem.books.domain.BorrowedRepository;

@Service
@RequiredArgsConstructor
public class BorrowedService {

    private final BorrowedRepository borrowedRepository;

    public void changeSignatureStatusTo(String status, String login, int id) {
        Borrowed borrowed = new Borrowed();
        borrowed.setLogin(login);
        borrowed.setSignatureId((long)id);
        borrowed.setStatus(status);
        System.out.println(borrowed);
        borrowedRepository.save(borrowed);
    }

}
