package pl.softsystem.books.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByOrderByTitle();

    List<Book> findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrAuthorsLastNameContainingIgnoreCaseOrAuthorsFirstNameContainingIgnoreCase(String title, String genre, String authorLastName, String authorFirstName);
    List<Book> findByTitleContainingIgnoreCaseOrAuthorsLastNameContainingIgnoreCaseOrAuthorsFirstNameContainingIgnoreCase(String title, String authorLastName, String authorFirstName);


    Book findBySignaturesContaining(Signature signature);
}
