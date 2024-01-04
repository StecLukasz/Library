package pl.apibooks.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.apibooks.books.domain.Borrowed;
import pl.apibooks.books.domain.BookRepository;
import pl.apibooks.books.domain.Borrowed;
import pl.apibooks.books.domain.BorrowedRepository;

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
