package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.softsystem.books.application.service.BookService;
import pl.softsystem.books.domain.Book;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class BookController {

    private final BookService bookService;

    @GetMapping()
    public List<Book> getBooks() {
        return bookService.getAll();
    }

    @GetMapping(ApiUrl.Book.FOR_USER)
    public List<Book> getBooksForUser(String login) {
        return bookService.getBooksBorrowedByUser(login);
    }

    @GetMapping(ApiUrl.Book.SEARCH)
    public List<Book> findBooks(String title, String genre, String authorLastName, String authorFirstName) {
        return bookService.findBooksByTitleAndGenreAndAuthor(title, genre, authorLastName, authorFirstName);
    }

    @GetMapping(ApiUrl.Book.RESERVED_FOR_USER)
    public List<Book> getReservedBooksForUser(String login) {
        return bookService.getBooksReservedByUser(login);
    }

    @PostMapping(ApiUrl.Book.RESERVE)
    public ResponseEntity<String> reserveBookByUser(@RequestBody Map<String, Object> data) {
        String login = (String) data.get("login");
        String title = (String) data.get("title");
        bookService.makeReservationBookByUser(login, title);
        return ResponseEntity.ok(title + " " + login);
    }

    @PostMapping(ApiUrl.Book.CANCEL_RESERVED)
    public ResponseEntity<String> cancelReservedBookByUser(@RequestBody Map<String, Object> data) {
        String login = (String) data.get("login");
        String title = (String) data.get("title");
        bookService.cancelReservedBookByUser(login, title);
        return ResponseEntity.ok(title + " " + login);
    }

    @GetMapping(ApiUrl.Book.IS_BOOK_RESERVED_BY_USER)
    public boolean isBookReservedByUser(String login, int bookId) {

        System.out.println(login + " " + bookId);

        return true;
    }

//    @GetMapping(ApiUrl.Book.SEARCH)
//    public List<Book> findBooks(String title, String genre, String authorLastName, String authorFirstName) {
//        return bookService.findBooksByTitleAndGenreAndAuthor(title, genre, authorLastName, authorFirstName);
//    }

}
