package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowedRepository borrowedRepository;

    public List<Book> getAll() {
        List<Book> books = bookRepository.findAllByOrderByTitle();
        books = countAvailableBooks(books);
        return books;
    }

    public List<Book> findBooksByTitleAndGenreAndAuthor(String title, String genre, String authorLastName) {
        List<Book> books = bookRepository
                .findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrAuthorsLastNameContainingIgnoreCase(title, genre, authorLastName);
        books = sortAuthorsByLastName(books);
        books = removeDuplicateBooks(books);
        books = sortBooksByTitle(books);
        books = countAvailableBooks(books);
        return books;
    }

    public List<Book> countAvailableBooks(List<Book> books) {
        for (Book book : books) {
            int count = 0;
            for (Signature signature : book.getSignatures()) {
                boolean isBorrowed = isLatestStatusBorrowed(signature.getBorrowedBookList());
                if (isBorrowed) count++;
            }
            book.setAvailableQuantity(countHowManySignatures(book) - count);
        }
        return books;
    }

    public boolean isLatestStatusBorrowed(List<Borrowed> borrowedList) {
        if (borrowedList == null || borrowedList.isEmpty()) {
            return false;
        }
        Optional<Borrowed> latestBorrowed = borrowedList.stream().max(Comparator.comparing(Borrowed::getStatusDate));
        return latestBorrowed.get().getStatus().equals("borrowed");
    }

    public List<Book> getBooksBorrowedByUser(String login) {
        // todo: get data from db for user which have login = login and return book list
        // query do bazy
//                return bookRepository.findAllByOrderByTitle();
        return new ArrayList<>();
    }

    public List<Book> getBooksReservedByUser(String login) {
        List<Book> allBooks = bookRepository.findAllByOrderByTitle();
        return allBooks.stream()
                .filter(book -> {
                    Optional<Borrowed> latestReserved = getLatestReserved(book, login);
                    return latestReserved.isPresent() && latestReserved.get().getStatus().equals("reserved");
                })
                .collect(Collectors.toList());
    }

    private Optional<Borrowed> getLatestReserved(Book book, String login) {
        return book.getSignatures().stream()
                .flatMap(signature -> signature.getBorrowedBookList().stream())
                .filter(borrowed -> borrowed.getLogin().equals(login))
                .max(Comparator.comparing(Borrowed::getStatusDate));
    }


    public static List<Book> removeDuplicateBooks(List<Book> books) {
        List<Book> uniqueBooks = new ArrayList<>();
        Set<String> addedTitles = new HashSet<>();

        for (Book book : books) {
            if (!addedTitles.contains(book.getTitle())) {
                uniqueBooks.add(book);
                addedTitles.add(book.getTitle());
            }
        }
        return uniqueBooks;
    }

    private static List<Book> sortAuthorsByLastName(List<Book> books) {
        books.forEach(book -> {
            List<Author> sortedAuthors = new ArrayList<>(book.getAuthors());
            sortedAuthors.sort(Comparator.comparing(Author::getLastName));
            book.setAuthors(new LinkedHashSet<>(sortedAuthors));
        });
        return books;
    }

    public static List<Book> sortBooksByTitle(List<Book> books) {
        Collections.sort(books, Comparator.comparing(Book::getTitle));
        return books;
    }

    public int countHowManySignatures(Book book) {
        List<Signature> signatures = book.getSignatures();
        return signatures.size();
    }

    public void makeReservationBookByUser(String login, String title) {
        List<Book> books = bookRepository.findAllByOrderByTitle();
        Book book = books.stream().filter(b -> b.getTitle().equals(title)).findFirst().orElse(null);
        Long availableSignatureId = getAvailableSignaturesQuantity(books, title);

//        int allSignaturesQuantity = book.getSignatures().size();
        if (getAvailableSignaturesQuantity(books, title) > 0) {

            Borrowed borrowed = new Borrowed();
            borrowed.setId(6L);
            borrowed.setLogin("jchyla");
            borrowed.setSignatureId(availableSignatureId);
            borrowed.setOverdueDate(new Date(System.currentTimeMillis()));
            borrowed.setStatus("nowystatus");

            System.out.println(borrowed);
            borrowedRepository.save(borrowed);

//            Signature fisrtAvailableSignatureToReserve = book.getSignatures().get(firstAvailableSignatureId(books, title).intValue());
//            List<Borrowed> borrowedList = fisrtAvailableSignatureToReserve.getBorrowedBookList();
//            Borrowed borrowed = new Borrowed();
////            borrowed.setId();
//            borrowed.setLogin(login);
//            borrowed.setSignatureId(firstAvailableSignatureId(books, title));
//            borrowed.setStatus("reserveddd");
//            borrowedList.add(borrowed);
//
//            book.setSignatures();
//            System.out.println(fisrtAvailableSignatureToReserve);
//            bookRepository.save(book);
////////////////////////////////////////////// new book to save
//            Book xBook = new Book();
//            xBook.setTitle("newbook");
//            xBook.setPages(123);
//            xBook.setGenre("gatunek");
//
//            ArrayList<Signature> xSignatures = new ArrayList<Signature>();
//            Signature xSignature = new Signature();
//            xSignature.setBookId(5L);
//            xSignature.setBookSignature("xSignature");
//            xSignatures.add(xSignature);
//
//            ArrayList<Borrowed> xBorroweds = new ArrayList<>();
//            Borrowed xBorrowed = new Borrowed();
//            xBorrowed.setLogin("xjchyla");
//            xBorrowed.setSignatureId(7L);
//            xBorrowed.setStatus("xstatus");
//            xBorroweds.add(xBorrowed);
//
//            xSignature.setBorrowedBookList(xBorroweds);
//            xBook.setSignatures(xSignatures);
//            System.out.println(xBook);
//
//            bookRepository.save(xBook);
        }
    }

    public Long getAvailableSignaturesQuantity(List<Book> books, String title) {
        Book bookByTitle = books.stream()
                .filter(b -> b.getTitle().equals(title)).findFirst().orElse(null);
        Long result = 0L;
        for (Signature signature : bookByTitle.getSignatures()) {
            if (signature.getBorrowedBookList().get(signature.getBorrowedBookList()
                    .size() - 1).getStatus().equals("available")) result++;
        }
        return result;
    }

    public Long firstAvailableSignatureId(List<Book> books, String title) {
        Book bookByTitle = books.stream()
                .filter(b -> b.getTitle().equals(title)).findFirst().orElse(null);
        Long result = -1L;
        for (Signature signature : bookByTitle.getSignatures()) {
            result++;
            if (signature.getBorrowedBookList().get(signature.getBorrowedBookList()
                    .size() - 1).getStatus().equals("available")) {
                break;
            }
        }
        return result;
    }

    public void changeToAvailableAfterOneWeek() {
        //TODO
        List<Book> books = bookRepository.findAllByOrderByTitle();
        System.out.println(books.get(0).getTitle());
    }
}

