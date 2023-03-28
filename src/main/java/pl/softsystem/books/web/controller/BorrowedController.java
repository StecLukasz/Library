package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.softsystem.books.application.service.BorrowedService;
import pl.softsystem.books.web.controller.constant.ApiUrl;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class BorrowedController {
    private final BorrowedService borrowedService;

    @PostMapping(ApiUrl.Book.CANCEL_SIGNATURE_RESERVATION)
    public ResponseEntity<String> cancelReservedBookByUser(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        String login = (String) data.get("login");
        int id = (int) data.get("id");
        borrowedService.cancelReservedSignatureByUser(login, id);
        return ResponseEntity.ok(login + " " + id);
    }

    @PostMapping(ApiUrl.Book.READY_SIGNATURE_RESERVATION)
    public ResponseEntity<String> readyReservedBookByUser(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        String login = (String) data.get("login");
        int id = (int) data.get("id");
        borrowedService.readyReservedSignatureByUser(login, id);
        return ResponseEntity.ok(login + " " + id);
    }

}
