package pl.softsystem.books.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowedRepository extends JpaRepository<Borrowed, Long> {
}
