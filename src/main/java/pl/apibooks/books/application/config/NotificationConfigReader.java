package pl.apibooks.books.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "notifications")
public class NotificationConfigReader {
    private Id id;

    @Getter @Setter
    public static class Id {
        private Long testNotification;
        private Long reservationNotification;
        private Long remindNotification;
    }

}
