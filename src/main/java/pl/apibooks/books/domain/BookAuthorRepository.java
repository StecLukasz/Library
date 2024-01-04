package pl.apibooks.books.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {

     BookAuthor findByBookId(Long bookId);
     @Transactional
     void deleteByBookId(Long bookId);
}
