package pl.softsystem.books.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "book_id")
    private Long bookId;

    @Column(name= "book_signature")
    private String bookSignature;

    @OneToMany(targetEntity = Borrowed.class, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "signature_id", referencedColumnName = "id")
    private List<Borrowed> borrowedBookList;
}
