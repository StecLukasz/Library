package pl.softsystem.books.application.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.softsystem.books.application.config.AppConfigReader;
import pl.softsystem.books.application.config.NotificationConfigReader;
import pl.softsystem.books.infrastructure.log.AppLogger;
import pl.softsystem.notifications.services.NotifyService;

@Service
@RequiredArgsConstructor
public class BookSender {

    private final AppConfigReader appConfigReader;
    private final NotificationConfigReader notificationConfigReader;
    private final NotifyService notifyService;

    public void sendTestNotification(String recipient, String message) {
        Long templateId = notificationConfigReader.getId().getTestNotification();

        if (templateId == null) {
            AppLogger.MAIL.warn("No notification template id assigned for notification, Notification wasn't send");
            return;
        }

        String appUrl = appConfigReader.getUrl();
        notifyService.send(templateId, new TestNotificationData(appUrl, recipient, message));
        AppLogger.MAIL.info("Test notification send successfully for {}", recipient);
    }


    @Getter
    @AllArgsConstructor
    static class TestNotificationData {
        private String url;
        private String recipient;
        private String message;
    }

}
