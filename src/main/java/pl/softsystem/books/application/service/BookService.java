package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.Book;
import pl.softsystem.books.domain.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAll() {
        return bookRepository.findAllByOrderByTitle();
    }

    public List<Book> findBooksByTitleAndGenre(String title, String genre) {
        return bookRepository.findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(title, genre);
    }
}

