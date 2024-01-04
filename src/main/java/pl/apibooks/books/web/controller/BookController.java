package pl.apibooks.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.apibooks.books.application.service.BookAuthorService;
import pl.apibooks.books.application.service.BookService;
import pl.apibooks.books.domain.Book;
import pl.apibooks.books.domain.ReservedSignaturesForUserDTO;
import pl.apibooks.books.domain.ResponseGenreDTO;
import pl.apibooks.books.domain.SearchDTO;
import pl.apibooks.books.application.service.SignatureService;
import pl.apibooks.books.domain.*;
import pl.apibooks.books.web.controller.constant.ApiUrl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final SignatureService signatureService;
    private final BorrowedRepository borrowedRepository;
    private final SignatureRepository signatureRepository;
    private final BookAuthorService bookAuthorService;

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
        bookService.editBook(bookId, bookDTO);
    }

    @GetMapping(ApiUrl.Book.GET_BOOK_BY_ID)
    public ResponseEntity<BookDTO> getBookForAdmin(@PathVariable Long bookId) {
        BookDTO book = bookService.getBookDTOById(bookId);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping(ApiUrl.Book.DELETE_SIGNATURE)
    public void deleteSignature(@PathVariable Long signatureId) {
        bookService.removeBook(signatureId);
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

    @GetMapping(ApiUrl.Book.SEARCH_FOR_ADMIN)
    public Set<SignatureDTO> getBooksSearchForAdmin(String title, String bookSignature) {
        return bookService.getBookTitleAndSignatureForAdmin(title, bookSignature);
    }
}
