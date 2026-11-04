package common;

import constant.Constant;
import core.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {

    /* Driver */
    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    /* Actions */
    public static WebElement findElement(By locator) {
        return driver().findElement(locator);
    }

    public static List<WebElement> findElements(By locator) {
        return driver().findElements(locator);
    }

    public static WebElement findElement(String locator, Object... args) {
        return findElement(By.xpath(String.format(locator, args)));
    }

    public static By getLocatorElement(String locator, Object... args) {
        return By.xpath(String.format(locator, args));
    }

    public static void clear(By locator) {
        waitForVisible(locator);
        findElement(locator).clear();
    }

    public static void enter(By locator, String string) {
        waitForVisible(locator);
        scrollToElement(locator);
        clear(locator);
        findElement(locator).sendKeys(string);
    }

    public static String getText(By locator) {
        waitForVisible(locator);
        return findElement(locator).getText();
    }

    public static String getText(String locator, Object... args) {
        By locatorElement = getLocatorElement(locator, args);
        waitForVisible(locatorElement);
        return findElement(locatorElement).getText();
    }

    public static void click(By locator) {
        waitForVisible(locator);
        scrollToElement(locator);
        waitForClickable(locator);
        findElement(locator).click();
    }

    public static void click(WebElement element) {
        scrollToElement(element);
        element.click();
    }

    /* Hover */
    public static void hover(By locator) {
        Actions actions = new Actions(driver());
        actions.moveToElement(findElement(locator)).perform();
    }

    /* Select */
    public static void selectByVisibleText(WebElement element, String text) {
        scrollToElement(element);
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    public static String getFirstSelectedText(WebElement element) {
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText().trim();
    }

    /* Scroll */
    public static void scrollToElement(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) driver();
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", findElement(locator));
    }

    public static void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver();
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    /* Wait */
    public static void waitForClickable(By locator) {
        waitForClickable(locator, Constant.TIMEOUT);
    }

    public static void waitForClickable(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitForVisible(By locator) {
        waitForVisible(locator, Constant.TIMEOUT);
    }

    public static void waitForVisible(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /* Handle Table */
    public static Map<String, Integer> getHeaderIndexMap(WebElement table) {

        List<WebElement> headers = table.findElements(By.tagName("th"));

        Map<String, Integer> headerIndexMap = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            headerIndexMap.put(headers.get(i).getText().trim(), i + 1);
        }

        return headerIndexMap;
    }

    public static String getCellText(WebElement row, Map<String, Integer> headerMap, String headerName) {

        Integer index = headerMap.get(headerName);
        if (index == null) {
            throw new RuntimeException("Header not found: " + headerName);
        }

        List<WebElement> cells = row.findElements(By.tagName("td"));

        return cells.get(index - 1).getText().trim();
    }

}
