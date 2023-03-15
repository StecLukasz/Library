package pl.softsystem.books.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long id;

    private String login;

    @Column(name = "signature_id")
    private Long signatureId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "borrowed_date")
    final private Date borrowedDate = new Date(System.currentTimeMillis());

    @Column(name = "overdue_date")
    private Date overdueDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "status_date")
    private Date statusDate = new Date(System.currentTimeMillis());

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "return_date")
    final private Date returnDate = new Date(System.currentTimeMillis());

    private String status;
}

