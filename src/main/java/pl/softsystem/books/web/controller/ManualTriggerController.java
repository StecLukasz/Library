package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.softsystem.books.application.notification.BookSender;
import pl.softsystem.books.web.controller.constant.ApiUrl;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.ManualTrigger.BASE)
public class ManualTriggerController {
    private final BookSender bookSender;

    @GetMapping(value = ApiUrl.ManualTrigger.SEND_TEST_NOTIFICATION)
    public String sendTestNotification(String recipient, String message) {
        bookSender.sendTestNotification(recipient, message);
        return "Send test notification successfully";
    }

    @GetMapping(value = ApiUrl.ManualTrigger.SEND_REQUEST_DENIED_NOTIFICATION)
    public String sendRequestDeniedNotification(String recipient, String message) {
        bookSender.sendRequestDeniedNotification(recipient, message);
        return "Send test notification successfully";
    }
}
