package pl.apibooks.books.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.apibooks.books.domain.User;
import pl.apibooks.books.domain.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User getUser(String username) {
        return repository.findByUsername(username);
    }


//    public List<User> getAllUsers() {
//        return repository.getAllUsers();
//    }
}
