package pl.softsystem.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.softsystem.notifications.EnableNotifications;
import pl.softsystem.security.EnableOAuthClientResources;
import pl.softsystem.security.EnableSessionOAuthSecurity;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableSessionOAuthSecurity
@EnableOAuthClientResources
@EnableScheduling
@EnableNotifications
@SpringBootApplication
public class BooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class, args);
    }

}
