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
    private final SignatureService signatureService;

    public List<Book> getAll() {
        List<Book> books = bookRepository.findAllByOrderByTitle();
        books = countAvailableBooks(books);
        books = sortAuthorsByLastName(books);
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

    public boolean isLatestStatusBorrowed(List<Borrowed> borrowedList) {
        if (borrowedList == null || borrowedList.isEmpty()) {
            return false;
        }
        Optional<Borrowed> latestBorrowed = borrowedList.stream().max(Comparator.comparing(Borrowed::getStatusDate));
        return latestBorrowed.get().getStatus().equals("borrowed");
    }

    public List<Book> getBorrowedDate(String login) {
        List<Book> borrowedBooks = new ArrayList<>();
        List<Book> allBooks = bookRepository.findAllByOrderByTitle();
        allBooks.forEach(book -> {
            Optional<Borrowed> latestBorrowed = getLatestBorrowed(book, login);
            if (latestBorrowed.isPresent() && latestBorrowed.get().getStatus().equals("borrowed")) {
                Borrowed borrowedBook = new Borrowed();
                borrowedBook.getBorrowedDate();
//                borrowedBooks.add(borrowedBook);
            }
        });
        return borrowedBooks;
    }

    public List<BookDTO> getBooksBorrowedByUserDto(String login) {
        List<Book> allBooks = bookRepository.findAllByOrderByTitle();
        List<BookDTO> borrowedBooks = new ArrayList<>();

        for (Book book : allBooks) {
            Optional<Borrowed> latestBorrowed = getLatestBorrowed(book, login);
            if (latestBorrowed.isPresent() && latestBorrowed.get().getStatus().equals("borrowed")) {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setBookId(book.getId());
                bookDTO.setTitle(book.getTitle());
                bookDTO.setBorrowedDate(latestBorrowed.get().getBorrowedDate());
                bookDTO.setReturnDate(addThreeMonthsToDate(latestBorrowed.get().getReturnDate())); //tutaj przypiszę metodę do oblicznia return date
                bookDTO.setStatus(latestBorrowed.get().getStatus());
                borrowedBooks.add(bookDTO);
            }
        }
        return borrowedBooks;
    }

    public Date addThreeMonthsToDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }

    public List<Book> countAvailableBooks(List<Book> books) {
        for (Book book : books) {
            int count = 0;
            for (Signature signature : book.getSignatures()) {
                boolean isBorrowed = isLatestStatusAvailable(signature.getBorrowedBookList());
                if (isBorrowed) count++;
            }
            book.setAvailableQuantity(count);
        }
        return books;
    }

    public boolean isLatestStatusAvailable(List<Borrowed> borrowedList) {
        if (borrowedList == null || borrowedList.isEmpty()) {
            return false;
        }
        Optional<Borrowed> latestBorrowed = borrowedList.stream().max(Comparator.comparing(Borrowed::getStatusDate));
        return latestBorrowed.get().getStatus().equals("available");
    }


    public List<Book> getBooksBorrowedByUser(String login) {
        List<Book> allBooks = bookRepository.findAllByOrderByTitle();
        return allBooks.stream()
                .filter(book -> {
                    Optional<Borrowed> latestBorrowed = getLatestBorrowed(book, login);
                    return latestBorrowed.isPresent() && latestBorrowed.get().getStatus().equals("borrowed");
                })
                .collect(Collectors.toList());
    }

    private Optional<Borrowed> getLatestBorrowed(Book book, String login) {
        return book.getSignatures().stream()
                .flatMap(signature -> signature.getBorrowedBookList().stream())
                .filter(borrowed -> borrowed.getLogin().equals(login))
                .max(Comparator.comparing(Borrowed::getStatusDate));
    }

    public List<Book> getBooksReservedByUser(String login) {
        List<Book> books = bookRepository.findAllByOrderByTitle();
        books = books.stream()
                .filter(book -> {
                    Optional<Borrowed> latestReserved = getLatestReserved(book, login);
                    return latestReserved.isPresent() && latestReserved.get().getStatus().equals("reserved");
                })
                .collect(Collectors.toList());
        books = sortAuthorsByLastName(books);

        return books;
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
        Long availableSignatureIndex = getAvailableSignaturesQuantity(books, title);

        if (availableSignatureIndex > 0) {

            Borrowed borrowed = new Borrowed();
            borrowed.setLogin(login);
            borrowed.setSignatureId(firstAvailableSignatureId(books, title, availableSignatureIndex));
            borrowed.setOverdueDate(new Date(System.currentTimeMillis()));
            borrowed.setStatus("reserved");

            System.out.println(borrowed);
            borrowedRepository.save(borrowed);
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

    public Long firstAvailableSignatureId(List<Book> books, String title, Long availableSignatureIndex) {
        Book bookByTitle = books.stream()
                .filter(b -> b.getTitle().equals(title)).findFirst().orElse(null);
        Long result = -1L;
        for (Signature signature : bookByTitle.getSignatures()) {
            if (signature.getBorrowedBookList().get(signature.getBorrowedBookList()
                    .size() - 1).getStatus().equals("available")) {
                result = signature.getId();
                break;
            }
        }
        System.out.println(result);
        return result;
    }

    public void changeToAvailableAfterOneWeek() {
        System.out.println("changeToAvailableAfterOneWeek started");
        List<Book> books = bookRepository.findAllByOrderByTitle();
        for (Book book : books) {
            for (Signature signature : book.getSignatures()) {
                Borrowed borrowed = signature.getBorrowedBookList().get(signature.getBorrowedBookList().size() - 1);
                if (borrowed.getStatus().equals("reserved")) {
                    if (isOneWeekLater(borrowed.getStatusDate())) {
                        Borrowed newBorrowed = new Borrowed();
                        newBorrowed.setLogin(borrowed.getLogin());
                        newBorrowed.setSignatureId(borrowed.getSignatureId());
                        newBorrowed.setOverdueDate(new Date(System.currentTimeMillis()));
                        newBorrowed.setStatus("available");
                        borrowedRepository.save(newBorrowed);
                    }
                }
            }
        }
    }

    public boolean isOneWeekLater(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar other = Calendar.getInstance();
        other.setTime(date);
        other.add(Calendar.WEEK_OF_YEAR, 1);
        return now.after(other);
    }

    public void addBook(BookDTO bookDTO) {

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setPages(bookDTO.getPages());
        book.setGenre(bookDTO.getGenre());
        List<Author> authors = new ArrayList<>();
        for (AuthorDTO authorDTO : bookDTO.getAuthorDTO()) {
            Author author = new Author();
            author.setFirstName(authorDTO.getFirstName());
            author.setLastName(authorDTO.getLastName());
            author.setGender(authorDTO.getGender());
            author.setBirthDate(authorDTO.getBirthDate());
            authors.add(author);
        }
        book.setAuthors((Set<Author>) authors);
        List<Signature> signatures = new ArrayList<>();
        for (AdminSignatureDTO adminSignatureDTO : bookDTO.getAdminSignatureDTO()) {
            Signature signature = new Signature();
            signature.setBookSignature(adminSignatureDTO.getBookSignature());
            List<Borrowed> borrowedList = new ArrayList<>();
            for (Borrowed borrowedDTO : adminSignatureDTO.getBorrowedBookList()) {
                Borrowed borrowed = new Borrowed();
                borrowed.setStatus("available");
                borrowedList.add(borrowed);
            }
            signatures.add(signature);
        }
        book.setSignatures(signatures);
        System.out.println(bookDTO);
        bookRepository.save(book);
    }


    public Book getLatestBook() {
        return bookRepository.findTopByOrderByIdDesc();
    }
}

