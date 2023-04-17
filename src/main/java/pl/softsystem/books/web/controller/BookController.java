package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.softsystem.books.application.service.BookService;
import pl.softsystem.books.domain.*;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;


    @GetMapping()
    public List<Book> getBooks() {
        return bookService.getAll();
    }

    @PostMapping(ApiUrl.Book.ADD_BOOK)
    public void addBookAdmin(@RequestBody BookDTO bookDTO) {
        System.out.println(bookDTO);
        bookService.addBook(bookDTO);
    }

    @PatchMapping(ApiUrl.Book.EDIT_BOOK)
    public void editBookAdmin(@PathVariable Long bookId, @RequestBody BookDTO bookDTO) {
        System.out.println(bookDTO);

        bookService.editBook(bookId, bookDTO);
    }

    @GetMapping(ApiUrl.Book.GET_BOOK_BY_ID)
    public ResponseEntity<Book> getBookForAdmin(@PathVariable Long bookId) {
        Book book = bookService.findById(bookId);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping(ApiUrl.Book.DELETE_BOOK)
    public ResponseEntity<Map<String, Boolean>> deleteBook(@PathVariable Long bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }


    @GetMapping(ApiUrl.Book.FOR_USER)
    public List<Book> getBooksBorrowedByUser(@RequestParam String login) {
        return bookService.getBooksBorrowedByUser(login);
    }

    @GetMapping(ApiUrl.Book.BORROWED_DATE)
    public List<BookDTO> getBooksBorrowedByUserOnlyForTable(@RequestParam String login) {
        List<BookDTO> borrowedBooks = bookService.getBooksBorrowedByUserDto(login);
        return borrowedBooks;
    }

    @GetMapping("/search")
    public List<Book> findBooks(String title, String genre, String authorLastName) {
        return bookService.findBooksByTitleAndGenreAndAuthor(title, genre, authorLastName);
    }

    @GetMapping(ApiUrl.Book.RESERVED_FOR_USER)
    public List<Book> getReservedBooksForUser(@RequestParam String login) {
        return bookService.getBooksReservedByUser(login);
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveBookByUser(@RequestBody Map<String, Object> data) {
        String login = (String) data.get("login");
        String title = (String) data.get("title");
        bookService.makeReservationBookByUser(login, title);
        return ResponseEntity.ok(title + " " + login);
    }

}
