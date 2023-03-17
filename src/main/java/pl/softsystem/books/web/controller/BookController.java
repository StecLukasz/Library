package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{login}/{bookId}")
    public ResponseEntity<String> reserveBookByUser(@PathVariable String login, @PathVariable Long bookId, @RequestBody Map<String, Object> data) {

        return ResponseEntity.ok("Book reserved successfully" + login + "/" + bookId);
    }
}
