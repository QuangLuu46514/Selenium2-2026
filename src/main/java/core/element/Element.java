package core.element;

import core.common.constant.TimeoutConstants;
import core.driver.DriverManager;
import core.enums.LocatorType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Element Wrapper: stores only a Locator and a readable name.
 * The WebElement is looked up lazily (on every action) from the current thread's driver,
 * so an Element can be declared once in a page object and reused safely in parallel runs.
 */
@Slf4j
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

    // ------------------------------------------------------- public wait API

    public Element waitUntilPresent() {
        return waitUntilPresent(TimeoutConstants.WAIT_TIMEOUT);
    }

    public Element waitUntilPresent(Duration timeout) {
        waitFor(ExpectedConditions.presenceOfElementLocated(getBy()), timeout, "present");
        return this;
    }

    public Element waitUntilVisible() {
        return waitUntilVisible(TimeoutConstants.WAIT_TIMEOUT);
    }

    public Element waitUntilVisible(Duration timeout) {
        waitFor(ExpectedConditions.visibilityOfElementLocated(getBy()), timeout, "visible");
        return this;
    }

    public Element waitUntilClickable() {
        return waitUntilClickable(TimeoutConstants.WAIT_TIMEOUT);
    }

    public Element waitUntilClickable(Duration timeout) {
        waitFor(ExpectedConditions.elementToBeClickable(getBy()), timeout, "clickable");
        return this;
    }

    public Element waitUntilInvisible() {
        return waitUntilInvisible(TimeoutConstants.WAIT_TIMEOUT);
    }

    public Element waitUntilInvisible(Duration timeout) {
        waitFor(ExpectedConditions.invisibilityOfElementLocated(getBy()), timeout, "invisible");
        return this;
    }

    public Element waitUntilTextContains(String text) {
        return waitUntilTextContains(text, TimeoutConstants.WAIT_TIMEOUT);
    }

    public Element waitUntilTextContains(String text, Duration timeout) {
        waitFor(ExpectedConditions.textToBePresentInElementLocated(getBy(), text), timeout,
                "containing text '" + text + "'");
        return this;
    }

    // ---------------------------------------------------------------- actions

    public Element click() {
        log.info("Click [{}]", name);
        findClickableElement().click();
        return this;
    }

    /**
     * Clears the input field and enters the provided text.
     */
    public Element type(String text) {
        log.info("Type '{}' into [{}]", text, name);
        WebElement element = findVisibleElement();
        element.clear();
        element.sendKeys(text);
        return this;
    }

    /**
     * Sends keys without clearing (e.g. Keys.ENTER).
     */
    public Element sendKeys(CharSequence... keys) {
        log.info("Send keys to [{}]", name);
        findVisibleElement().sendKeys(keys);
        return this;
    }

    public String getText() {
        return findVisibleElement().getText().trim();
    }

    /**
     * Reads an HTML attribute as written in the DOM, e.g. getAttribute("href").
     */
    public String getAttribute(String attributeName) {
        return findPresentElement().getDomAttribute(attributeName);
    }

    public Element selectByVisibleText(String text) {
        log.info("Select '{}' in [{}]", text, name);
        new Select(findVisibleElement()).selectByVisibleText(text);
        return this;
    }

    public String getSelectedText() {
        return new Select(findVisibleElement()).getFirstSelectedOption().getText().trim();
    }

    public Element scrollIntoView() {
        log.info("Scroll into view [{}]", name);
        ((JavascriptExecutor) driver()).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", findPresentElement());
        return this;
    }

    public Element hover() {
        log.info("Hover [{}]", name);
        new Actions(driver()).moveToElement(findVisibleElement()).perform();
        return this;
    }

    /**
     * Click via JavaScript when the element is covered by an overlay or an ad banner.
     */
    public Element jsClick() {
        log.info("JS click [{}]", name);
        ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", findPresentElement());
        return this;
    }

    // ------------------------------------------------------------------ state

    /**
     * Checks immediately, never throws.
     */
    public boolean isDisplayed() {
        try {
            List<WebElement> found = driver().findElements(getBy());
            return !found.isEmpty() && found.getFirst().isDisplayed();
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * Waits up to timeout for the element to become visible, never throws.
     */
    public boolean isDisplayed(Duration timeout) {
        try {
            waitUntilVisible(timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public boolean isEnabled() {
        return findPresentElement().isEnabled();
    }

    public boolean isSelected() {
        return findPresentElement().isSelected();
    }

    /**
     * Number of matching elements right now (0 if none).
     */
    public int count() {
        return driver().findElements(getBy()).size();
    }

    @Override
    public String toString() {
        return name + " (" + locator + ")";
    }
}

