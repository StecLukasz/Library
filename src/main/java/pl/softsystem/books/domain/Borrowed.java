package pl.softsystem.books.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "borrowed")
public class Borrowed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrowed_id")
    private Long borrowedId;

    @Column(name = "User_id")
    private Long userId;

    @Column(name = "borrowed_date")
    private Date borrowedDate;

    @Column(name = "returnt_date")
    private Date returntDate;

    @OneToMany(targetEntity = Book.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "borrowed_id")
    private List<Book> bookList;
}
