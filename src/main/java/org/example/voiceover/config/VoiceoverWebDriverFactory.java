package org.example.voiceover.config;


import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VoiceoverWebDriverFactory {

    @Value("${voiceover.selenium.firefoxProfilePath}")
    private String firefoxProfilePath;

    @Value("${voiceover.selenium.downloadDir}")
    private String downloadDir;

    public WebDriver createWebDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-profile", firefoxProfilePath);
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", downloadDir);
        options.addPreference("browser.download.useDownloadDir", true);
        options.addPreference("browser.download.viewableInternally.enabledTypes", "");
        options.addPreference("browser.download.forbid_open_with", true);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "audio/mpeg");

        return new FirefoxDriver(options);
    }
}