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

    private String role;

    @Column(name = "signature_id")
    private String signatureId;

    @Column(name = "borrowed_date")
    private Date borrowedDate;
    
    @Column(name = "overdue_date")
    private Date overdueDate;

    @Column(name = "returnt_date")
    private Date returntDate;
    
    private String status;

}
