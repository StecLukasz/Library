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

    public List<SearchDTO> findBooksByTitleAndGenreAndAuthorForUser(String title, String genre, String authorLastName, String authorFirstName, String login) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrAuthorsLastNameContainingIgnoreCaseOrAuthorsFirstNameContainingIgnoreCase(title, genre, authorLastName, authorFirstName);
        books = sortAuthorsByLastName(books);
        books = removeDuplicateBooks(books);
        books = sortBooksByTitle(books);
        books = countAvailableBooks(books);
        books = getLastBookStatusForUser(books, login);
        List<SearchDTO> searchBooks = searchBooksMapper(books);
        return searchBooks;
    }

    public List<SearchDTO> searchBooksMapper(List<Book> books) {
        List<SearchDTO> searchBooks = new ArrayList<>();
        for (Book book : books) {
            SearchDTO searchDTO = new SearchDTO();
            searchDTO.setTitle(book.getTitle());
            searchDTO.setGenre(book.getGenre());
            searchDTO.setAvailableQuantity(book.getAvailableQuantity());
            searchDTO.setSignatureQuantity(book.getSignatures().size());
            searchDTO.setAuthors(book.getAuthors());
            searchDTO.setBookStatusForUser(book.getBookStatusForUser());
            searchBooks.add(searchDTO);
        }

        return searchBooks;
    }

    public List<Book> getLastBookStatusForUser(List<Book> books, String login) {
        List<Book> availableBooks = books;

        for (int i = 0; i < availableBooks.size(); i++) {
            for (Signature signature : availableBooks.get(i).getSignatures()) {
                List<Borrowed> borrowedBookList = signature.getBorrowedBookList();
                Borrowed latestBorrowed = borrowedBookList.get(borrowedBookList.size() - 1);
                if (login.equals(latestBorrowed.getLogin())) {
                    availableBooks.get(i).setBookStatusForUser(latestBorrowed.getStatus());
                }
            }
        }
        return availableBooks;
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

    public List<ReservedSignaturesForUserDTO> getBooksReservedByUser(String login) {
        List<Book> books = bookRepository.findAllByOrderByTitle();
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            Optional<Borrowed> latestReserved = getLatestReserved(book, login);
            if (latestReserved.isPresent() && (latestReserved.get().getStatus().equals("reserved")||
                    latestReserved.get().getStatus().equals("ready"))) {
                filteredBooks.add(book);
            }
        }
        filteredBooks = sortAuthorsByLastName(filteredBooks);
        return reservedUserBookMapper(filteredBooks, login);
    }

    private Optional<Borrowed> getLatestReserved(Book book, String login) {
        return book.getSignatures().stream()
                .flatMap(signature -> signature.getBorrowedBookList().stream())
                .filter(borrowed -> borrowed.getLogin().equals(login))
                .max(Comparator.comparing(Borrowed::getStatusDate));
    }

    public List<ReservedSignaturesForUserDTO> reservedUserBookMapper(List<Book> books, String login) {
        List<ReservedSignaturesForUserDTO> reservedBooks = new ArrayList<>();

        for (Book book : books) {
            System.out.println(book.getSignatures());

            ReservedSignaturesForUserDTO reservedBook = new ReservedSignaturesForUserDTO();
            reservedBook.setTitle(book.getTitle());
            reservedBook.setGenre(book.getGenre());
            reservedBook.setAuthors(book.getAuthors());
            reservedBook.setStatus(getLastBookStatusForUser(book, login));
            reservedBooks.add(reservedBook);
        }
        return reservedBooks;
    }

    public String getLastBookStatusForUser(Book book, String login) {
        String result = "";
        for (Signature signature : book.getSignatures()) {
            List<Borrowed> borrowedList = signature.getBorrowedBookList();
            if (borrowedList.get(borrowedList.size() - 1).getLogin().equals(login)) {
                result = borrowedList.get(borrowedList.size() - 1).getStatus();
            }
        }
        return result;
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
        if (availableSignatureIndex > 0 && isLastStatusNotEqualsTo("reserved", login, title)) {
            Borrowed borrowed = new Borrowed();
            borrowed.setLogin(login);
            borrowed.setSignatureId(firstAvailableSignatureId(books, title, availableSignatureIndex));
            borrowed.setOverdueDate(new Date(System.currentTimeMillis()));
            borrowed.setStatus("reserved");
            System.out.println(borrowed);
            borrowedRepository.save(borrowed);
        }
    }

    public boolean isLastStatusNotEqualsTo(String lastStatus, String login, String title) {
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
        System.out.println("isLastStatusNotEqualsTo is: " + result + ". and laststatus is not: " + lastStatus);
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
        if (isLastStatusNotEqualsTo("available", login, title)) {
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

