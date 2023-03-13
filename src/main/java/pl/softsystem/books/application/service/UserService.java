package pl.softsystem.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.domain.User;
import pl.softsystem.books.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User getUser(String username) {
        return repository.findByUsername(username);
    }

}
