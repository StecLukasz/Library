package pl.apibooks.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.apibooks.books.application.config.AppConfigReader;
import pl.apibooks.books.infrastructure.log.AppLogger;
import pl.apibooks.books.shared.dto.AppInfo;
import pl.apibooks.books.web.controller.constant.ApiUrl;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.INFO)
public class AppController {
    private final AppConfigReader appConfigReader;

    @GetMapping()
    public AppInfo getAppInfo() {
        return new AppInfo(appConfigReader);
    }
}
