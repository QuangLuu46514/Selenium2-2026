package core.element;

import core.enums.LocatorType;
import org.openqa.selenium.By;

/**
 * Where to find an element: a locator type and its value, e.g. {@code Locator.id("login-btn")}.
 *
 * <p>Immutable, so one Locator can be shared by page objects running in parallel threads.
 * {@link #toString()} gives a short readable form ("ID=login-btn") for logs and error messages.
 */

public class Locator {
    private final LocatorType type;
    private final String value;

    public Locator(LocatorType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Locator id(String id) {
        return new Locator(LocatorType.ID, id);
    }

    public static Locator name(String name) {
        return new Locator(LocatorType.NAME, name);
    }

    public static Locator xpath(String xpath) {
        return new Locator(LocatorType.XPATH, xpath);
    }

    public static Locator css(String css) {
        return new Locator(LocatorType.CSS, css);
    }

    public static Locator className(String className) {
        return new Locator(LocatorType.CLASS_NAME, className);
    }

    public static Locator tagName(String tagName) {
        return new Locator(LocatorType.TAG_NAME, tagName);
    }

    public static Locator linkText(String text) {
        return new Locator(LocatorType.LINK_TEXT, text);
    }

    public static Locator partialLinkText(String text) {
        return new Locator(LocatorType.PARTIAL_LINK_TEXT, text);
    }

    /**
     * Dynamic XPath: fills the %s placeholders, e.g. dynamicXpath("//a[@data-nav='%s']", "products").
     */
    public static Locator dynamicXpath(String xpathTemplate, Object... values) {
        return xpath(String.format(xpathTemplate, values));
    }

    /**
     * The Selenium By used to find the element.
     */
    public By getBy() {
        return type.toBy(value);
    }

    @Override
    public String toString() {
        return type + "=" + value;
    }
}
