package tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import utils.ScreenshotWatcher;
import webdriver.CreateWebDriver;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExampleTests {
    public static WebDriver driver;

    @RegisterExtension
    public ScreenshotWatcher watcher = new ScreenshotWatcher(driver);

    @BeforeAll
    static void setUp() {
        driver = new CreateWebDriver().createLocalDriver();
    }

    @Test
    @Order(1)
    @DisplayName("Passed test")
    void examplePassed() {
        log.info("Success test start");
        driver.navigate().to("https://amazon.com/");
        log.info("Success test finish");
    }

    @Test
    @Order(2)
    @DisplayName("Failed test")
    void exampleFailed() {
        log.info("Failed test start");
        driver.navigate().to("https://google.com/error");
        Assertions.fail("Caught error");
    }

    @Test
    @Order(3)
    @DisplayName("Handling console error 500")
    void exampleHandling() {
        log.info("500 error test start");
        driver.navigate().to("https://office.com/error");
        Assertions.fail("Caught error 500");
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            log.info("Webdriver exists, I'll try to close it");
            driver.quit();
        }
    }
}
