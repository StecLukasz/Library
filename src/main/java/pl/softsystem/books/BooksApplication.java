package pl.softsystem.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.softsystem.security.EnableOAuthClientResources;
import pl.softsystem.security.EnableSessionOAuthSecurity;

@EnableSessionOAuthSecurity
@EnableOAuthClientResources
@SpringBootApplication
public class BooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class, args);
    }

}
