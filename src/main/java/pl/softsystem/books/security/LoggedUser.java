package pl.softsystem.books.security;

import org.springframework.security.core.GrantedAuthority;
import pl.softsystem.security.helpers.LoggedUserDetails;

import java.util.Collection;
import java.util.List;

public class LoggedUser extends LoggedUserDetails {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String domain;
    private List<GrantedAuthority> authorities;

    public LoggedUser(String firstName, String lastName, String username, String domain, List<GrantedAuthority> authorities) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.domain = domain;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
