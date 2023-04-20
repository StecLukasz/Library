package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.softsystem.books.application.service.BookService;
import pl.softsystem.books.application.service.SignatureService;
import pl.softsystem.books.domain.*;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import javax.transaction.Transactional;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final SignatureService signatureService;
    private final BorrowedRepository borrowedRepository;
    private final SignatureRepository signatureRepository;


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

    @PostMapping(ApiUrl.Book.DELETE_SIGNATURE)
    public void deleteOneSignature(@PathVariable Long signatureId) {
        Signature signature = signatureRepository.getById(signatureId);
        Book book = bookRepository.findBySignaturesContaining(signature);

        book.getSignatures().remove(signature);
        bookRepository.save(book);

        signatureRepository.delete(signature);
    }


    @DeleteMapping(ApiUrl.Book.DELETE_BOOK)
    public void deleteBook(@PathVariable Long id) {
        Book book = bookRepository.getById(id);
        bookRepository.deleteById(book.getId());

        for(Signature signature: book.getSignatures()){
            for (Borrowed borrowed: signature.getBorrowedBookList()){
                borrowedRepository.delete(borrowed);
            }
            signatureRepository.delete(signature);
        }
        bookRepository.deleteById(id);
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
