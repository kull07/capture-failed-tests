package utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Slf4j
public class ConsoleLogErrors {
    WebDriver driver;
    public static List<String> errors = new ArrayList<>();

    public ConsoleLogErrors(WebDriver driver) {
        this.driver = driver;
    }

    public void getErrorsFromConsole() {
        Set<String> logTypes = driver.manage().logs().getAvailableLogTypes();
        List<LogEntry> toPrint;
        for (String logType : logTypes) {
            List<LogEntry> allLog = driver.manage().logs().get(logType).getAll();
            if (allLog.isEmpty()) {
                continue;
            }
            toPrint = allLog.stream().
                    filter(Predicate.not(x -> x.getMessage().contains("500"))).
                    filter(Predicate.not(x -> x.getMessage().contains("Any part of the error to skip"))).toList();
            toPrint.stream().map(LogEntry::getMessage).forEach(errors::add);
            if (!errors.isEmpty()) {
                log.info("===============CONSOLE LOG=================");
                log.info("{} ERRORS:", logType.toUpperCase());
                errors.forEach(log::info);
                log.info("-----------------------------------------");
            }
        }
    }

    public void clearConsoleErrors() {
        driver.manage().logs().get(LogType.BROWSER).getAll();
        errors.clear();
    }
}
