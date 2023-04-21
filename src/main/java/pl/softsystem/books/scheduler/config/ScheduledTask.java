package pl.softsystem.books.scheduler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import pl.softsystem.books.application.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import pl.softsystem.books.application.service.SignatureService;
import pl.softsystem.books.application.service.UserService;

@Configuration
public class ScheduledTask {

    @Autowired
    private BookService bookService;
    private UserService userService;
    private SignatureService signatureService;

//    @Scheduled(cron = "*/30 * * * * ?")
        @Scheduled(cron = "0 0 4 * * ?")
    public void setStatusAvailable() throws InterruptedException {
        bookService.changeStatusToAvailableAfterOneWeek();
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void reminderNotification() throws InterruptedException {
       bookService.sendReminderTwoWeeksBeforeDueDate("login");

    }


}

