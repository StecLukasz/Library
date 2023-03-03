package pl.softsystem.books.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.softsystem.books.application.config.AppConfigReader;

@Getter
@AllArgsConstructor
public class AppInfo {
    private String url;
    private String name;
    private String version;
    private String companyName;

    public AppInfo(AppConfigReader data) {
        this.url = data.getUrl();
        this.name = data.getBuild().getName();
        this.version = data.getBuild().getVersion();
        this.companyName = data.getInfo().getCompanyName();
    }

}
