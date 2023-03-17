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
    public List<Book> getBooksForUser(@RequestParam String login) {
        return bookService.getBooksBorrowedByUser(login);
    }

    @GetMapping("/search")
    public List<Book> findBooks(String title, String genre, String authorLastName) {
        return bookService.findBooksByTitleAndGenreAndAuthor(title, genre, authorLastName);
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveBookByUser(@RequestBody Map<String, Object> data) {
        String login = (String) data.get("login");
        String title = (String) data.get("title");
        System.out.println(login);
        System.out.println(title);
        bookService.makeReservationBookByUser(login, title);
        return ResponseEntity.ok("Book " + title + " reserved successfully for " + login);
    }
}
