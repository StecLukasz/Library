package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.application.notification.BookSender;
import pl.softsystem.books.domain.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
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

    public List<SearchDTO> findBooksWithGenreList(String title, String genre, String authorLastName, String authorFirstName, String login) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorsLastNameContainingIgnoreCaseOrAuthorsFirstNameContainingIgnoreCase(title, authorLastName, authorFirstName);
        books = sortAuthorsByLastName(books);
        books = removeDuplicateBooks(books);
        books = sortBooksByTitle(books);
        books = countAvailableBooks(books);
        books = getLastBookStatusForUser(books, login);
        books = removeEveryGenreExcept(books, genre);
        List<SearchDTO> searchBooks = searchBooksMapper(books);
        return searchBooks;
    }

    public List<Book> removeEveryGenreExcept(List<Book> books, String genre) {
        List<Book> result = new ArrayList<>();
        if (!genre.equals("")) {
            for (Book book : books) {
                if (book.getGenre().equals(genre)) {
                    result.add(book);
                }
            }
        } else result = books;
        return result;
    }

    public List<SearchDTO> searchBooksMapper(List<Book> books) {
        List<SearchDTO> searchBooks = new ArrayList<>();
        for (Book book : books) {
            SearchDTO searchDTO = new SearchDTO();
            searchDTO.setTitle(book.getTitle());
            searchDTO.setGenre(book.getGenre());
            searchDTO.setAvailableQuantity(book.getAvailableQuantity());
            searchDTO.setSignatureQuantity(book.getSignatures().size());

            Set<AuthorAdminDTO> authorAdminDTOs = new HashSet<>();
            for (Author author : book.getAuthors()) {
                AuthorAdminDTO authorAdminDTO = new AuthorAdminDTO();
                authorAdminDTO.setFirstName(author.getFirstName());
                authorAdminDTO.setLastName(author.getLastName());
                authorAdminDTOs.add(authorAdminDTO);
                searchDTO.setAuthors(authorAdminDTOs);
            }

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

    public List<ReservedSignaturesForUserDTO> getBooksReservedByUser(String login) {
        List<Book> books = bookRepository.findAllByOrderByTitle();
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            Optional<Borrowed> latestReserved = getLatestReserved(book, login);
            if (latestReserved.isPresent() && (latestReserved.get().getStatus().equals("reserved") ||
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
    public boolean inOneMinute(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar other = Calendar.getInstance();
        other.setTime(date);
        other.add(Calendar.MINUTE, 1);
        return now.after(other);
    }

    public Set<ResponseGenreDTO> getGenreDTOList(){
        List<Book> books = bookRepository.findAllByOrderByTitle();
        HashSet<ResponseGenreDTO> genreDTOs = new HashSet<>();
        for(Book book : books){
            ResponseGenreDTO genreDTO = new ResponseGenreDTO();
            genreDTO.setGenre(book.getGenre());
            genreDTOs.add(genreDTO);
        }
        return genreDTOs;
    }

    //TODO
    public Set<SignatureDTO> getBookTitleAndSignatureForAdmin(String title, String bookSignature) {
        Set<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrSignaturesBookSignatureContainingIgnoreCase(title, bookSignature);
        List<Signature> signatures = signatureRepository.findAll();
        Set<SignatureDTO> adminSignatureDTOS = new HashSet<>();

        for (Book book : books) {
            for (Signature signature : book.getSignatures()) {
                SignatureDTO signatureDTO = new SignatureDTO();
                String titleBySignatureIf = getTitleBySignatureId(books, signature.getId());
                signatureDTO.setId(signature.getId());
                signatureDTO.setTitle(titleBySignatureIf);
                signatureDTO.setBookId(signature.getBookId());
                signatureDTO.setBookSignature(signature.getBookSignature());
                signatureDTO.setUsername(getUsernameForLatestStatus(signature));
                signatureDTO.setStatus(getLatestStatusForSignature(signature));
                adminSignatureDTOS.add(signatureDTO);
            }
        }
        return adminSignatureDTOS;
    }
    public String getTitleBySignatureId(Set<Book> books, Long signatureId) {
        for (Book book : books) {
            for (Signature signature : book.getSignatures()) {
                if (signature.getId().equals(signatureId)) {
                    return book.getTitle();
                }
            }
        }
        return null;
    }

    public String getUsernameForLatestStatus(Signature signature) {
        List<Borrowed> borrowedList = signature.getBorrowedBookList();
        if (borrowedList == null || borrowedList.isEmpty()) return null;
        Borrowed latestBorrowed = borrowedList.get(0);
        for (Borrowed borrowed : borrowedList) {
            if (borrowed.getStatusDate().compareTo(latestBorrowed.getStatusDate()) > 0) {
                latestBorrowed = borrowed;
            }
        }
        return latestBorrowed.getLogin();
    }

    public String getLatestStatusForSignature(Signature signature) {
        List<Borrowed> borrowedList = signature.getBorrowedBookList();
        if (borrowedList == null || borrowedList.isEmpty()) return null;
        Borrowed latestBorrowed = borrowedList.get(0);
        for (Borrowed borrowed : borrowedList) {
            if (borrowed.getStatusDate().compareTo(latestBorrowed.getStatusDate()) > 0) {
                latestBorrowed = borrowed;
            }
        }
        return latestBorrowed.getStatus();
    }
}

