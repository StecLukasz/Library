package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.softsystem.books.application.service.BookService;
import pl.softsystem.books.domain.Book;
import pl.softsystem.books.domain.ReservedSignaturesForUserDTO;
import pl.softsystem.books.domain.ResponseGenreDTO;
import pl.softsystem.books.domain.SearchDTO;
import pl.softsystem.books.domain.*;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public ResponseEntity<BookDTO> getBookForAdmin(@PathVariable Long bookId) {
        BookDTO book = bookService.getBookDTOById(bookId);
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
    public List<Book> getBooksForUser(String login) {
        return bookService.getBooksBorrowedByUser(login);
    }

    @GetMapping(ApiUrl.Book.SEARCH)
    public List<SearchDTO> findBooks(String title, String genre, String authorLastName, String authorFirstName, String login) {
        return bookService.findBooksByTitleAndGenreAndAuthorForUser(title, genre, authorLastName, authorFirstName, login);
    }

    @GetMapping(ApiUrl.Book.SEARCH_WITH_GENRE_LIST)
    public List<SearchDTO> findBooksWithGenreList(String title, String genre, String authorLastName, String authorFirstName, String login) {
        return bookService.findBooksWithGenreList(title, genre, authorLastName, authorFirstName, login);
    }

    @GetMapping(ApiUrl.Book.RESERVED_FOR_USER)
    public List<ReservedSignaturesForUserDTO> getReservedBooksForUser(String login) {
        return bookService.getBooksReservedByUser(login);
    }

    @PostMapping(ApiUrl.Book.RESERVE)
    public ResponseEntity<Integer> reserveBookByUser(@RequestBody Map<String, Object> data) {
        String login = (String) data.get("login");
        String title = (String) data.get("title");
        bookService.makeReservationBookByUser(login, title);
        return ResponseEntity.ok(1);
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

    @GetMapping(ApiUrl.Schedule.RUN_SCHEDULER_AVAILABLE_AFTER_ONE_WEEK)
    public String runSchedulerChangeStatusToAvailableAfterOneWeek(){
        bookService.changeStatusToAvailableAfterOneWeek();
        return "changeStatusToAvailableAfterOneWeek started: " + LocalDateTime.now();
    }

    @GetMapping(ApiUrl.Book.GENRE_LIST)
    public Set<ResponseGenreDTO> getGenreDTOList(){
        return bookService.getGenreDTOList();
    }

}
