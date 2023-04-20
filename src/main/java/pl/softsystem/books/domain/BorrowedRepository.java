package pl.softsystem.books.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowedRepository extends JpaRepository<Borrowed, Long> {

//    @Query("SELECT b FROM Book b JOIN b.signatures s JOIN s.borrowedBookList bb WHERE bb.login = :login AND bb.status = :status")
//    List<Book> findBooksBorrowedByUser(@Param("login") String login, @Param("status") String status);
//

    List<Borrowed> findByLogin(String login);

    List<Borrowed> findAllByStatus(String borrowed);

    List<Borrowed> findByStatus(String borrowed);
}



