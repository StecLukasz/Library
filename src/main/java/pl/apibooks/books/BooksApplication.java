package pl.apibooks.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.apibooks.notifications.EnableNotifications;
import pl.apibooks.security.EnableOAuthClientResources;
import pl.apibooks.security.EnableSessionOAuthSecurity;
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
