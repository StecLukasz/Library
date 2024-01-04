package pl.apibooks.books.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfigReader {
    private String url;
    private Build build;
    private Info info;

    @Getter @Setter
    public static class Build {
        private String name;
        private String version;
    }

    @Getter @Setter
    public static class Info {
        private String companyName;
    }

}
