package pl.apibooks.books.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.apibooks.books.shared.type.RoleType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "newhr_employee_view")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "first_name_pl")
    private String firstNamePl;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "last_name_pl")
    private String lastNamePl;

    private String email;

    private String department;

    @Column(name = "employment_form")
    private Byte employmentForm;

    @Column(name = "manager_id")
    private Long managerId;

    @Enumerated(EnumType.STRING)
    private RoleType role;

}
