package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.Author;
import pl.softsystem.books.domain.Book;
import pl.softsystem.books.domain.BookRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAll() {
        List<Book> books = bookRepository.findAllByOrderByTitle();

        return sortAuthorsByLastName(books);
    }

    public List<Book> findBooksByTitleAndGenreAndAuthor(String title, String genre, String authorLastName) {
        List<Book> books = bookRepository
                .findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrAuthorsLastNameContainingIgnoreCase(title, genre, authorLastName);

        return sortAuthorsByLastName(books);
    }

    public List<Book> findBooksByTitleAndGenre(String title, String genre) {
        return bookRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(title, genre);
    }

    public List<Book> getBooksBorrowedByUser(String login) {
        // todo: get data from db for user which have login = login and return book list
        // query do bazy
//                return bookRepository.findAllByOrderByTitle();
        return new ArrayList<>();
    }

    private static List<Book> sortAuthorsByLastName(List<Book> books) {
        books.forEach(book -> {
            List<Author> sortedAuthors = new ArrayList<>(book.getAuthors());
            sortedAuthors.sort(Comparator.comparing(Author::getLastName));
            book.setAuthors(new LinkedHashSet<>(sortedAuthors));
        });
        return books;
    }
}

