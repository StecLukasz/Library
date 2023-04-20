package pl.softsystem.books.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.softsystem.books.application.config.AppConfigReader;
import pl.softsystem.books.infrastructure.log.AppLogger;
import pl.softsystem.books.shared.dto.AppInfo;
import pl.softsystem.books.web.controller.constant.ApiUrl;

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
