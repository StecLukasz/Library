package pl.softsystem.books.scheduler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import pl.softsystem.books.application.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class ScheduledTask {

    @Autowired
    private BookService bookService;

//    @Scheduled(cron = "*/30 * * * * ?")
        @Scheduled(cron = "0 0 4 * * ?")
    public void computePrice() throws InterruptedException {
        bookService.changeStatusToAvailableAfterOneWeek();
    }
}

