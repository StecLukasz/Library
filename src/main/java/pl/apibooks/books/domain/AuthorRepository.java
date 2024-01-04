package pl.apibooks.books.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>  {

    Author findByFirstNameAndLastName(String firstName, String lastName);
}
