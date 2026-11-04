package core.driver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import core.common.constant.TimeoutConstants;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Driver Wrapper: browser-level actions for the current thread.
 *
 * <p>Page objects and tests call {@code Browser.navigateTo("login.html")} instead of
 * {@code DriverManager.getDriver().get(...)}. Logging, waits and URL building are centralized here.
 */
@Slf4j
public final class Browser {

    private static WebDriverWait wait(Duration timeout) {
        return new WebDriverWait(driver(), timeout, TimeoutConstants.POLLING_INTERVAL);
    }

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    // ---- Windows / Navigation ----
    public static void navigateTo(String url) {
        log.info("Navigate to {}", url);
        driver().get(url);
        waitForPageLoaded();
    }

    public static void maximize() {
        log.info("Maximize window");
        driver().manage().window().maximize();
    }

    public static void refresh() {
        log.info("Refresh page");
        driver().navigate().refresh();
        waitForPageLoaded();
    }

    public static void back() {
        log.info("Press Back");
        driver().navigate().back();
        waitForPageLoaded();
    }

    public static void forward() {
        log.info("Press Forward");
        driver().navigate().forward();
        waitForPageLoaded();
    }

    public static String getCurrentUrl() {
        return driver().getCurrentUrl();
    }

    public static String getTitle() {
        return driver().getTitle();
    }

    public static void waitForPageLoaded() {
        waitForPageLoaded(TimeoutConstants.PAGE_LOAD_TIMEOUT);
    }

    public static void waitForPageLoaded(Duration timeout) {
        wait(timeout).until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    // ---- Alert ----
    public static Alert waitForAlert() {
        return wait(TimeoutConstants.WAIT_TIMEOUT).until(ExpectedConditions.alertIsPresent());
    }

    public static void acceptAlert() {
        log.info("Accept alert");
        waitForAlert().accept();
    }

    public static void dismissAlert() {
        log.info("Dismiss alert");
        waitForAlert().dismiss();
    }

    public static String getAlertText() {
        return waitForAlert().getText();
    }

    // ---- Tabs / Windows ----
    public static void openNewTab(String url) {
        log.info("Open new tab: {}", url);
        driver().switchTo().newWindow(WindowType.TAB);
        navigateTo(url);
    }

    public static void switchToWindow(int index) {
        List<String> handles = new ArrayList<>(driver().getWindowHandles());
        driver().switchTo().window(handles.get(index));
    }

    public static void switchToLastWindow() {
        List<String> handles = new ArrayList<>(driver().getWindowHandles());
        driver().switchTo().window(handles.getLast());
    }

    // ---- Switch to window by title ----
    public static void switchToWindow(String titleContains) {
        String original = driver().getWindowHandle();
        for (String handle : driver().getWindowHandles()) {
            driver().switchTo().window(handle);
            if (driver().getTitle().contains(titleContains)) return;
        }
        driver().switchTo().window(original);
        throw new NoSuchWindowException("No window title contains: " + titleContains);
    }

    public static void closeCurrentWindow() {
        driver().close();
    }

    // ---- JavaScript ----

    public static void executeScript(String script, Object... args) {
        ((JavascriptExecutor) driver()).executeScript(script, args);
    }

    // ---- Screenshot ----

    public static byte[] takeScreenshot() {
        return ((TakesScreenshot) driver()).getScreenshotAs(OutputType.BYTES);
    }

    private Browser() {
        // Prevent instantiation.
    }
}


