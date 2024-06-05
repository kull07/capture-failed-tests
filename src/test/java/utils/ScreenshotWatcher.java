package utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class ScreenshotWatcher implements TestWatcher {
    String path;
    WebDriver driver;
    ConsoleLogErrors consoleLogErrors;

    public ScreenshotWatcher(WebDriver driver) {
        this.path = "./build/test-results/test";
        this.driver = driver;
        consoleLogErrors = new ConsoleLogErrors(driver);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable throwable) {
        consoleLogErrors.getErrorsFromConsole();
        consoleLogErrors.clearConsoleErrors();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> optional) {
        optional.ifPresent(r -> log.info("Reason: {}", r));
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        consoleLogErrors.getErrorsFromConsole();
        consoleLogErrors.clearConsoleErrors();
        captureScreenshot(driver, context);
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        consoleLogErrors.clearConsoleErrors();
    }

    public void captureScreenshot(WebDriver driver, ExtensionContext context) {
        String pathToFile = path + File.separator + context.getRequiredTestClass().getName();
        String fullPath = pathToFile + File.separator + context.getDisplayName() + ".png";
        try {
            new File(pathToFile).mkdirs();
            try (FileOutputStream out = new FileOutputStream(fullPath)) {
                out.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            }
        } catch (IOException | WebDriverException e) {
            log.info("screenshot failed:" + e.getMessage());
        }
    }
}
