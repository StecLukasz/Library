package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.*;

import java.time.LocalDateTime;
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
        books = sortAuthorsByLastName(books);
        return books;
    }

    public List<Book> findBooksByTitleAndGenreAndAuthor(String title, String genre, String authorLastName, String authorFirstName, String login) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrAuthorsLastNameContainingIgnoreCaseOrAuthorsFirstNameContainingIgnoreCase(title, genre, authorLastName, authorFirstName);
        books = sortAuthorsByLastName(books);
        books = removeDuplicateBooks(books);
        books = sortBooksByTitle(books);
        books = countAvailableBooks(books);
        books = getLastBookStatusForUser(books, login);
        return books;
    }

    public List<Book> getLastBookStatusForUser(List<Book> books, String login) {
        //String login = "dmichna";
        List<Book> availableBooks = books;

        for (int i = 0; i < availableBooks.size(); i++) {
            for (Signature signature : availableBooks.get(i).getSignatures()) {
                if (isSignatureReservedByUser(signature, login)) {
                    availableBooks.get(i).setBookStatusForUser("reserved");
                }
            }
        }
        return availableBooks;
    }

    public boolean isSignatureReservedByUser(Signature signature, String login) {
        List<Borrowed> borrowedBookList = signature.getBorrowedBookList();
        Borrowed latestBorrowed = borrowedBookList.get(borrowedBookList.size() - 1);
        if ("reserved".equals(latestBorrowed.getStatus()) && login.equals(latestBorrowed.getLogin())) {
            return true;
        }
        return false;
    }


    public List<Book> countAvailableBooks(List<Book> books) {
        for (Book book : books) {
            int count = 0;
            for (Signature signature : book.getSignatures()) {
                boolean isAvailable = isLatestStatusAvailableForBorrowedList(signature.getBorrowedBookList());
                if (isAvailable) count++;
            }
            book.setAvailableQuantity(count);
        }
        return books;
    }

    public boolean isLatestStatusAvailableForBorrowedList(List<Borrowed> borrowedList) {
        if (borrowedList == null || borrowedList.isEmpty()) {
            return false;
        }
        Optional<Borrowed> latestBorrowed = borrowedList.stream().max(Comparator.comparing(Borrowed::getStatusDate));
        return latestBorrowed.get().getStatus().equals("available");
    }

    public List<Book> getBooksBorrowedByUser(String login) {
        // todo: get data from db for user which have login = login and return book list
        // query do bazy
//                return bookRepository.findAllByOrderByTitle();
        return new ArrayList<>();
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


    public List<Book> removeDuplicateBooks(List<Book> books) {
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

    private List<Book> sortAuthorsByLastName(List<Book> books) {
        books.forEach(book -> {
            List<Author> sortedAuthors = new ArrayList<>(book.getAuthors());
            sortedAuthors.sort(Comparator.comparing(Author::getLastName));
            book.setAuthors(new LinkedHashSet<>(sortedAuthors));
        });
        return books;
    }

    public List<Book> sortBooksByTitle(List<Book> books) {
        Collections.sort(books, Comparator.comparing(Book::getTitle));
        return books;
    }

    public void makeReservationBookByUser(String login, String title) {
        List<Book> books = bookRepository.findAllByOrderByTitle();
        Long availableSignatureIndex = getAvailableSignaturesQuantity(books, title);

        if (availableSignatureIndex > 0 && isLastStatusEqualsTo("reserved", login, title)) {

            Borrowed borrowed = new Borrowed();
            borrowed.setLogin(login);
            borrowed.setSignatureId(firstAvailableSignatureId(books, title, availableSignatureIndex));
            borrowed.setOverdueDate(new Date(System.currentTimeMillis()));
            borrowed.setStatus("reserved");

            System.out.println(borrowed);
            borrowedRepository.save(borrowed);
        }
    }

    public boolean isLastStatusEqualsTo(String lastStatus, String login, String title) {
        boolean result = true;
        List<Book> books = bookRepository.findAllByOrderByTitle();
        Book book = books.stream().filter(b -> b.getTitle().equals(title)).findFirst().orElse(null);
        for (Signature signature : book.getSignatures()) {
            int end = signature.getBorrowedBookList().size() - 1;
            if (signature.getBorrowedBookList().get(end).getStatus().equals(lastStatus)
                    && signature.getBorrowedBookList().get(end).getLogin().equals(login)) {
                result = false;
            }
        }
        return result;
    }

    public boolean isSignatureNotAvailableByUser(String login, String title) {
        boolean result = true;
        List<Book> books = bookRepository.findAllByOrderByTitle();
        Book book = books.stream().filter(b -> b.getTitle().equals(title)).findFirst().orElse(null);
        for (Signature signature : book.getSignatures()) {
            int end = signature.getBorrowedBookList().size() - 1;
            if (signature.getBorrowedBookList().get(end).getStatus().equals("available")
                    && signature.getBorrowedBookList().get(end).getLogin().equals(login)) {
                System.out.println(signature);
                result = false;
            }
        }
        return result;
    }

    public void cancelReservedBookByUser(String login, String title) {
        List<Book> books = bookRepository.findAllByOrderByTitle();
        Book book = books.stream().filter(b -> b.getTitle().equals(title)).findFirst().orElse(null);
        Signature reservedSignatureByUser = null;
        for (Signature signature : book.getSignatures()) {
            for (Borrowed borrowed : signature.getBorrowedBookList()) {
                if (borrowed.getStatus().equals("reserved") && borrowed.getLogin().equals(login)) {
                    if (reservedSignatureByUser == null ||
                            borrowed.getStatusDate().compareTo(reservedSignatureByUser.getBorrowedBookList().get(0).getStatusDate()) > 0) {
                        reservedSignatureByUser = signature;
                    }
                }
            }
        }
        if (isLastStatusEqualsTo("available", login, title)) {
            Borrowed borrowed = new Borrowed();
            borrowed.setLogin(login);
            borrowed.setSignatureId(reservedSignatureByUser.getId());
            borrowed.setOverdueDate(new Date(System.currentTimeMillis()));
            borrowed.setStatus("available");
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

    public void changeStatusToAvailableAfterOneWeek() {
        System.out.println("changeToAvailableAfterOneWeek started: " + LocalDateTime.now());
        List<Book> books = bookRepository.findAllByOrderByTitle();
        for (Book book : books) {
            for (Signature signature : book.getSignatures()) {
                Borrowed borrowed = signature.getBorrowedBookList().get(signature.getBorrowedBookList().size() - 1);
                if (borrowed.getStatus().equals("ready")) {
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

    public boolean inOneMinute(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar other = Calendar.getInstance();
        other.setTime(date);
        other.add(Calendar.MINUTE, 1);
        return now.after(other);
    }


}

