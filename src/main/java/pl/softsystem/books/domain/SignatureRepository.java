package pl.softsystem.books.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {

    Signature findByBookSignature(String bookSignature);

    Signature findByBookId(Long bookId);
    Optional<Signature> findByBookIdAndBookSignature(Long bookId, String bookSignature);


}
