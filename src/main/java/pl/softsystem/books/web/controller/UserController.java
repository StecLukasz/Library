package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.softsystem.books.application.service.UserService;
import pl.softsystem.books.domain.User;
import pl.softsystem.books.web.controller.constant.ApiUrl;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.USERS)
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return this.userService.getUser(username);
    }
}
