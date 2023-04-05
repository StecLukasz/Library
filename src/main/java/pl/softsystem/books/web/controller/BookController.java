package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.softsystem.books.application.service.BookService;
import pl.softsystem.books.domain.Book;
import pl.softsystem.books.domain.BookDTO;
import pl.softsystem.books.domain.BookRepository;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class BookController {

    private final BookService bookService;
    private BookRepository bookRepository;


    @GetMapping()
    public List<Book> getBooks() {
        return bookService.getAll();
    }


    @PostMapping(ApiUrl.Book.ADD_BOOK)
    public void addBookAdmin(@RequestBody Book book){
        System.out.println(book);
        System.out.println(book);
        bookService.addBook(book);
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
