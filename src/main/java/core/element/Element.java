package core.element;

import core.common.constant.TimeoutConstants;
import core.driver.DriverManager;
import core.enums.LocatorType;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

/**
 * Element Wrapper: stores only a Locator and a readable name.
 * The WebElement is looked up lazily (on every action) from the current thread's driver,
 * so an Element can be declared once in a page object and reused safely in parallel runs.
 */

public class Element {
    private final Locator locator;
    private final String name;
// ------------------------------------------------------------ constructors

    public Element(Locator locator, String name) {
        this.locator = locator;
        this.name = name;
    }

    public Element(Locator locator) {
        this(locator, locator.toString());
    }

    public Element(LocatorType type, String value) {
        this(new Locator(type, value));
    }

    public Element(String xpath) {
        this(Locator.xpath(xpath));
    }

    /**
     * dynamic("//a[text()='%s']", "Menu [%s]", "Products")
     */
    public static Element dynamic(String xpathTemplate, String nameTemplate, Object... args) {
        return new Element(Locator.dynamicXpath(xpathTemplate, args), String.format(nameTemplate, args));
    }

    public By getBy() {
        return locator.getBy();
    }

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    // ------------------------------------------------------ internal wait helpers

    private <T> T waitFor(Function<WebDriver, T> condition, Duration timeout, String state) {
        try {
            return new WebDriverWait(driver(), timeout, TimeoutConstants.POLLING_INTERVAL).until(condition);
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("[%s] was not %s after %ds. Locator: %s",
                    name, state, timeout.toSeconds(), locator), e);
        }
    }

    private WebElement findPresentElement() {
        return waitFor(ExpectedConditions.presenceOfElementLocated(getBy()), TimeoutConstants.WAIT_TIMEOUT, "present");
    }

    private WebElement findVisibleElement() {
        return waitFor(ExpectedConditions.visibilityOfElementLocated(getBy()), TimeoutConstants.WAIT_TIMEOUT, "visible");
    }

    private WebElement findClickableElement() {
        return waitFor(ExpectedConditions.elementToBeClickable(getBy()), TimeoutConstants.WAIT_TIMEOUT, "clickable");
    }
}
