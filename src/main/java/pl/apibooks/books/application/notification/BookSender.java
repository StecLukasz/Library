package pl.apibooks.books.application.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.apibooks.books.application.config.AppConfigReader;
import pl.apibooks.books.application.config.NotificationConfigReader;
import pl.apibooks.books.infrastructure.log.AppLogger;
import pl.apibooks.notifications.services.NotifyService;

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

    public void sendRequestDeniedNotification(Long templateId, String recipient, String message) {

        if (templateId == null) {
            AppLogger.MAIL.warn("No notification template id assigned for notification, Notification wasn't send");
            return;
        }

        String appUrl = appConfigReader.getUrl();
        notifyService.send(templateId, new TestNotificationData(appUrl, recipient, message));
        AppLogger.MAIL.info("Test notification send successfully for {}", recipient);
    }

    public void sendBookReadyNotification(Long templateId, String recipient, String message) {

        if (templateId == null) {
            AppLogger.MAIL.warn("No notification template id assigned for notification, Notification wasn't send");
            return;
        }

        String appUrl = appConfigReader.getUrl();
        notifyService.send(templateId, new TestNotificationData(appUrl, recipient, message));
        AppLogger.MAIL.info("Test notification send successfully for {}", recipient);
    }

    public void sendRemindingNotification(Long l, String recipient, String message) {
        Long templateId = notificationConfigReader.getId().getTestNotification();

        if (templateId == null) {
            AppLogger.MAIL.warn("No notification template id assigned for notification, Notification wasn't send");
            return;
        }

        String appUrl = appConfigReader.getUrl();
        notifyService.send(l, new TestNotificationData(appUrl, recipient, message));
        AppLogger.MAIL.info("Test notification send successfully for {}", recipient);
    }
    public void sendRemindingFromAdminNotification(Long l, String recipient, String message) {
        Long templateId = notificationConfigReader.getId().getTestNotification();

        if (templateId == null) {
            AppLogger.MAIL.warn("No notification template id assigned for notification, Notification wasn't send");
            return;
        }

        String appUrl = appConfigReader.getUrl();
        notifyService.send(l, new TestNotificationData(appUrl, recipient, message));
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
