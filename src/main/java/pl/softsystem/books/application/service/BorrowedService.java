package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.BookRepository;
import pl.softsystem.books.domain.Borrowed;
import pl.softsystem.books.domain.BorrowedRepository;

@Service
@RequiredArgsConstructor
public class BorrowedService {

    private final BorrowedRepository borrowedRepository;

    //TODO refactor to DRY
    public void cancelReservedSignatureByUser(String login, int id) {
        Borrowed borrowed = new Borrowed();
        borrowed.setLogin(login);
        borrowed.setSignatureId((long)id);
        borrowed.setStatus("available");
        System.out.println(borrowed);
        borrowedRepository.save(borrowed);
    }

    public void readyReservedSignatureByUser(String login, int id) {
        Borrowed borrowed = new Borrowed();
        borrowed.setLogin(login);
        borrowed.setSignatureId((long)id);
        borrowed.setStatus("ready");
        System.out.println(borrowed);
        borrowedRepository.save(borrowed);
    }

    public void borrowReservedSignatureByUser(String login, int id) {
        Borrowed borrowed = new Borrowed();
        borrowed.setLogin(login);
        borrowed.setSignatureId((long)id);
        borrowed.setStatus("borrowed");
        System.out.println(borrowed);
        borrowedRepository.save(borrowed);
    }
}
