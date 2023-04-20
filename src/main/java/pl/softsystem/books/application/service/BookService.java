package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.application.notification.BookSender;
import pl.softsystem.books.domain.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final SignatureRepository signatureRepository;
    private final BorrowedRepository borrowedRepository;
    private final SignatureService signatureService;
    private BookSender bookSender;
    private final UserService userService;

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

    public void sendReminderTwoWeeksBeforeDueDate(String login) {
        List<BookDTO> borrowedBooks = getBooksBorrowedByUserDto(login);
        for (BookDTO book : borrowedBooks) {
            Date dueDate = book.getReturnDate();
            LocalDate now = LocalDate.now();
            long daysUntilDue = ChronoUnit.DAYS.between(now, dueDate.toInstant());
            if (daysUntilDue == 14) {
                // send reminder to user
                bookSender.sendRemindingNotification(208L, login, "Your time of borrowing book is already ending");
            }
        }
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

//    public void sendReminderTwoWeeksBeforeDueDate() {
//        pobrac z bazy liste sygnatur które sa borrowed i do jego loginu
//        wysłał maila zapisujac je do listy a potem do sheludera


//        List<String> usersWithBooksToReturn = new ArrayList<>();
//        List<User> allUsers = userService.getAllUsers();
//        for (User user : allUsers) {
//            List<Book> borrowedBooks = getBooksBorrowedByUser(user.getUsername());
//            Date currentDate = new Date();
//            for (Book book : borrowedBooks) {
//                Optional<Borrowed> latestBorrowed = getLatestBorrowed(book, user.getUsername());
//                if (latestBorrowed.isPresent()) {
//                    Date dueDate = addThreeMonthsToDate(latestBorrowed.get().getStatusDate());
//                    Date reminderDate = new Date(dueDate.getTime() - (2 * 7 * 24 * 60 * 60 * 1000L)); // odejmujemy 2 tygodnie w milisekundach
//                    if (currentDate.after(reminderDate) && currentDate.before(dueDate)) {
//                        usersWithBooksToReturn.add(user.getUsername());
//                        break; // przerwij pętlę po znalezieniu pierwszej książki do oddania
//                    }
//                }
//            }
//        }
//        // Wyślij przypomnienia tylko do tych użytkowników, którzy mają książki do oddania
//        for (String login : usersWithBooksToReturn) {
//            bookSender.sendRemindingNotification(208L, login, "Twój czas na oddanie książki już się kończy");
//        }
//    }

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

        System.out.println(bookDTO);
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setPages(bookDTO.getPages());
        book.setGenre(bookDTO.getGenre());
        Set<Author> authors = new HashSet<>();
        for (AuthorDTO authorDTO : bookDTO.getAuthorDTO()) {
            Author author = new Author();
//            author.setId(authorDTO.getId());
            author.setFirstName(authorDTO.getFirstName());
            author.setLastName(authorDTO.getLastName());
            author.setGender(authorDTO.getGender());
            author.setBirthDate(authorDTO.getBirthDate());
            authors.add(author);
//            authorRepository.save(author);
        }
        book.setAuthors(authors);

        List<Signature> signatures = new ArrayList<>();
        for (AdminSignatureDTO adminSignatureDTO : bookDTO.getAdminSignatureDTO()) {
            Signature signature = new Signature();
            signature.setBookId(1L);
            signature.setBookSignature(adminSignatureDTO.getBookSignature());

            Borrowed borrowed = new Borrowed();
            borrowed.setStatus("available");
            borrowed.setSignatureId(1L);
            borrowed.setLogin("none");
            List<Borrowed> borrowedList = new ArrayList<>();
            borrowedList.add(borrowed);
            signature.setBorrowedBookList(borrowedList);

            signatures.add(signature);
        }
        book.setSignatures(signatures);

        System.out.println(bookDTO);
        System.out.println(book);
        bookRepository.save(book);
    }


    @Transactional
    public void editBook(Long bookId, BookDTO bookDTO) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

        bookDTO.setBookId(bookId);
        /** Edytuj atrybuty książki */
        book.setTitle(bookDTO.getTitle());
        book.setPages(bookDTO.getPages());
        book.setGenre(bookDTO.getGenre());

        /** Edytuj autorów */
        Set<AuthorDTO> authorDTOs = bookDTO.getAuthorDTO();
        saveOrUpdateAuthors(authorDTOs);
        // pobierz aktualne encje Author z bazy danych na podstawie informacji z DTO
        Set<Author> updatedAuthors = new HashSet<>();
        for (AuthorDTO authorDTO : authorDTOs) {
            Author author = authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName());
            if (author == null) {
                throw new EntityNotFoundException("Author not found with name: " + authorDTO.getFirstName() + " " + authorDTO.getLastName());
            }
            updatedAuthors.add(author);
        }
        book.setAuthors(updatedAuthors);

        /** Edytuj sygnatury */
        List<Signature> signatures = book.getSignatures();
        Long idSignarute = getSignatureIdByTitle(bookRepository.findAllByOrderByTitle(), bookDTO.getTitle());

        for (AdminSignatureDTO adminSignatureDTO : bookDTO.getAdminSignatureDTO()) {
            adminSignatureDTO.setId(idSignarute);

            for (Signature signature : signatures) {
                if (signature.getId() == idSignarute) {
                    signature.setBookSignature(adminSignatureDTO.getBookSignature());
                }
            }
        }
        bookRepository.save(book);
    }

    public void saveOrUpdateAuthors(Set<AuthorDTO> authorDTOs) {
        for (AuthorDTO authorDTO : authorDTOs) {
            Author author = authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName());
            if (author == null) {
                author = new Author();
                author.setFirstName(authorDTO.getFirstName());
                author.setLastName(authorDTO.getLastName());
            }
            author.setGender(authorDTO.getGender());
            author.setBirthDate(authorDTO.getBirthDate());
            authorRepository.save(author);
        }
    }

    public Long getSignatureIdByTitle(List<Book> books, String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                for (Signature signature : book.getSignatures()) {
                    return signature.getId(); // zwracamy pierwsze znalezione id sygnatury
                }
            }
        }
        return null; // zwracamy null jeśli nie udało się znaleźć sygnatury o określonym tytule
    }


    public Book findById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));
    }

    public BookDTO getBookDTOById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));

        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setPages(book.getPages());
        bookDTO.setGenre(book.getGenre());

        List<AdminSignatureDTO> adminSignatureDTOs = new ArrayList<>();
        for (Signature signature : book.getSignatures()) {
            AdminSignatureDTO adminSignatureDTO = new AdminSignatureDTO();
            adminSignatureDTO.setBookSignature(signature.getBookSignature());

            Set<AuthorDTO> authorDTOS = new HashSet<>();
            for (Author author : book.getAuthors()) {
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setFirstName(author.getFirstName());
                authorDTO.setLastName(author.getLastName());
                authorDTO.setGender(author.getGender());
                authorDTO.setBirthDate(author.getBirthDate());
                authorDTOS.add(authorDTO);
            }

            adminSignatureDTOs.add(adminSignatureDTO);
            bookDTO.setAuthorDTO(authorDTOS);
        }
        bookDTO.setAdminSignatureDTO(adminSignatureDTOs);

        return bookDTO;
    }


    public List<BookDTO> getSignaturesForAdminList() {
        List<BookDTO> adminBookDTO = new ArrayList<>();
        List<Book> books = bookRepository.findAllByOrderByTitle();

        for (int i = 0; i < books.size(); i++) {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setBookId(books.get(i).getId());
            bookDTO.setTitle(books.get(i).getTitle());
            bookDTO.setPages(books.get(i).getPages());
            bookDTO.setGenre(books.get(i).getGenre());

            List<AdminSignatureDTO> adminSignatureDTOs = new ArrayList<>();
            for (Signature signature : books.get(i).getSignatures()) {
                AdminSignatureDTO adminSignatureDTO = new AdminSignatureDTO();
                adminSignatureDTO.setBookSignature(signature.getBookSignature());

                List<AuthorDTO> authorDTOS = new ArrayList<>();
                for (Author author : books.get(i).getAuthors()) {
                    AuthorDTO authorDTO = new AuthorDTO();
                    authorDTO.setFirstName(author.getFirstName());
                    authorDTO.setLastName(author.getLastName());
                    authorDTO.setGender(author.getGender());
                    authorDTO.setBirthDate(author.getBirthDate());
                    authorDTOS.add(authorDTO);
                }
                adminSignatureDTOs.add(adminSignatureDTO);
            }
            bookDTO.setAdminSignatureDTO(adminSignatureDTOs);

            adminBookDTO.add(bookDTO);
        }

        return adminBookDTO;
    }

}

