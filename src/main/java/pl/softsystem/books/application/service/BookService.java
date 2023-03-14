package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.Book;
import pl.softsystem.books.domain.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAll() {
        return bookRepository.findAllByOrderByTitle();
    }

    public List<Book> getBooksBorrowedByUser(String login) {
        // todo: get data from db for user which have login = login and return book list
        // query do bazy
//                return bookRepository.findAllByOrderByTitle();
        return new ArrayList<>();
    }

    public List<Book> findBooksByTitleAndGenre(String title, String genre) {
        return bookRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(title, genre);
    }
}

