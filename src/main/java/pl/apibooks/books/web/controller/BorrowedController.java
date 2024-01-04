package pl.apibooks.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.apibooks.books.application.notification.BookSender;
import pl.apibooks.books.application.service.BorrowedService;
import pl.apibooks.books.web.controller.constant.ApiUrl;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.Book.BASE)
public class BorrowedController {
    private final BorrowedService borrowedService;
    private final BookSender bookSender;

    @PostMapping(ApiUrl.Book.CANCEL_SIGNATURE_RESERVATION)
    public ResponseEntity<String> cancelReservedBookByUser(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        String login = (String) data.get("login");
        int id = (int) data.get("id");
        borrowedService.changeSignatureStatusTo("available", login, id);
        bookSender.sendRequestDeniedNotification(205L, login, "Your request denied, contact with librarian.");
        return ResponseEntity.ok(login + " " + id);
    }

    @PostMapping(ApiUrl.Book.READY_SIGNATURE_RESERVATION)
    public ResponseEntity<String> readyReservedBookByUser(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        String login = (String) data.get("login");
        int id = (int) data.get("id");
        borrowedService.changeSignatureStatusTo("ready", login, id);
        bookSender.sendBookReadyNotification(206L, login, "Book is ready!");
        return ResponseEntity.ok(login + " " + id);
    }

    @PostMapping(ApiUrl.Book.BORROW_SIGNATURE)
    public ResponseEntity<String> borrowReservedBookByUser(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        String login = (String) data.get("login");
        int id = (int) data.get("id");
        borrowedService.changeSignatureStatusTo("borrowed", login, id);
        return ResponseEntity.ok(login + " " + id);
    }

    @PostMapping(ApiUrl.Book.BORROW_SIGNATURE_AVAILABLE)
    public ResponseEntity<String> returnOfTheBook(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        String login = (String) data.get("login");
        int id = (int) data.get("id");
        borrowedService.changeSignatureStatusTo("available", login, id);
        return ResponseEntity.ok(login + " " + id);
    }

    @PostMapping(ApiUrl.Book.BORROW_SIGNATURE_AVAILABLE_REMINDER)
    public ResponseEntity<String> reminderOfTheBook(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        String login = (String) data.get("login");
        int id = (int) data.get("id");
        bookSender.sendRemindingFromAdminNotification(209L, login, "Your time of borrowing book is already ending");
        return ResponseEntity.ok(login + " " + id);
    }
}
