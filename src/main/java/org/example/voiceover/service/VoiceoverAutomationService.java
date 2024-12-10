package org.example.voiceover.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.voiceover.config.VoiceoverWebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceoverAutomationService {

    private final VoiceoverWebDriverFactory webDriverFactory;
    private WebDriver driver;

    @Value("${voiceover.site.url}")
    private String siteUrl;

    @Value("${voiceover.site.voice}")
    private String voiceName;

    @Value("${voiceover.site.speed}")
    private String speedValue;

    @Value("${voiceover.site.initialWaitSeconds}")
    private int initialWaitSeconds;

    @Value("${voiceover.site.retryWaitSeconds}")
    private int retryWaitSeconds;

    @Value("${voiceover.selenium.downloadDir}")
    private String downloadDir;

    private void initDriver() {
        if (driver == null) {
            driver = webDriverFactory.createWebDriver();
        }
    }

    public String processVoiceover(String text) {
        String downloadedFilePath = null;
        try {
            initDriver();
            driver.get(siteUrl);

            WebElement textArea = driver.findElement(By.id("voice-text"));
            textArea.clear();
            textArea.sendKeys(text);
            log.info("Text inserted: {}", text);
            selectSpeed(speedValue);
            log.info("Speed selected: {}", speedValue);
            selectVoice(voiceName);
            log.info("Voice selected: {}", voiceName);
            Set<String> beforeDownloadFiles = listFiles(downloadDir);
            WebElement ozvuchitButton = driver.findElement(By.cssSelector("div#txt-to-voice span"));
            ozvuchitButton.click();
            log.info("Clicked 'Озвучить'");
            Thread.sleep(initialWaitSeconds * 1000L);

            boolean downloadedAppeared = isDownloadButtonPresent();
            if (!downloadedAppeared) {
                log.info("'Скачать' not appeared, waiting another {} seconds...", retryWaitSeconds);
                Thread.sleep(retryWaitSeconds * 1000L);
                downloadedAppeared = isDownloadButtonPresent();
            }
            if (downloadedAppeared) {
                WebElement downloadButton = driver.findElement(By.cssSelector("div.audio-download"));
                downloadButton.click();
                log.info("'Скачать' clicked");
                // Ждём появления нового файла в папке загрузок
                String newFile = waitForNewFile(downloadDir, beforeDownloadFiles, 30); // Ждём до 30 секунд
                if (newFile != null) {
                    downloadedFilePath = Paths.get(downloadDir, newFile).toString();
                    log.info("File downloaded: {}", downloadedFilePath);
                } else {
                    log.warn("No new file appeared in the download directory.");
                }
            } else {
                log.warn("'Скачать' not appeared after retries");
            }

        } catch (Exception e) {
            log.error("Automation error", e);
        } finally {
            closeBrowser();
        }
        return downloadedFilePath;
    }

    private void selectSpeed(String speed) {
        WebElement speedContainer = driver.findElement(By.cssSelector("span.speed.voice-option div.chosen-container"));
        WebElement speedChosenElement = speedContainer.findElement(By.cssSelector("a.chosen-single"));
        speedChosenElement.click();
        WebElement speedOption = speedContainer.findElement(By.xpath(".//li[contains(text(), '" + speed + "')]"));
        speedOption.click();
    }

    private void selectVoice(String voice) {
        WebElement voiceContainer = driver.findElement(By.cssSelector("div.chosen-container.voice-image"));
        WebElement chosenElement = voiceContainer.findElement(By.cssSelector("a.chosen-single"));
        chosenElement.click();
        WebElement voiceOption = voiceContainer.findElement(By.xpath(".//li[contains(text(), '" + voice + "')]"));
        voiceOption.click();
    }

    private boolean isDownloadButtonPresent() {
        try {
            driver.findElement(By.cssSelector("div.audio-download"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private Set<String> listFiles(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] files = dir.listFiles();
        if (files == null) return new HashSet<>();
        Set<String> fileNames = new HashSet<>();
        for (File f : files) {
            fileNames.add(f.getName());
        }
        return fileNames;
    }

    private String waitForNewFile(String directoryPath, Set<String> existingFiles, int timeoutSeconds) throws InterruptedException {
        int waited = 0;
        while (waited < timeoutSeconds) {
            Set<String> currentFiles = listFiles(directoryPath);
            currentFiles.removeAll(existingFiles);
            if (!currentFiles.isEmpty()) {
                return currentFiles.iterator().next();
            }
            Thread.sleep(1000);
            waited++;
        }
        return null;
    }

    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}