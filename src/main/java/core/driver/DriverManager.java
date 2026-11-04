package core.driver;

import core.config.DriverConfig;
import core.enums.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * Owns the WebDriver of the <b>current thread</b>.
 *
 * <p>Every TestNG worker thread gets its own WebDriver through {@link ThreadLocal}, so parallel tests
 * never share a browser. The life cycle is: {@link #initDriver} in {@code @BeforeMethod},
 * {@link #getDriver} anywhere in the test, {@link #quitDriver} in {@code @AfterMethod}.
 */
@Slf4j
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void initDriver(String browser) {
        DriverConfig driverConfig = new DriverConfig();

        if (browser != null && !browser.isBlank()) {
            driverConfig.setBrowser(BrowserType.from(browser));
        }

        WebDriver driver = DriverFactory.createDriver(driverConfig);
        DRIVER.set(driver);
    }

    /** Returns the driver bound to the current thread or fails fast with a clear message. */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized for thread '"
                    + Thread.currentThread().getName() + "'. Call DriverManager.initDriver() first.");
        }
        return driver;
    }

    /** Quits the browser and always clears the ThreadLocal, even if quit() throws. */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
            log.info("Driver quit on thread {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.warn("Error while quitting driver: {}", e.getMessage());
        } finally {
            DRIVER.remove();
        }
    }

    private DriverManager() {
        // Prevent instantiation.
    }
}

