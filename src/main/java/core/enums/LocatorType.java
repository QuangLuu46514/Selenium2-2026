package core.enums;

import org.openqa.selenium.By;
import core.element.Locator;

/** The locator strategies supported by {@link Locator}; each one knows how to build its Selenium {@link By} */

public enum LocatorType {
    ID, NAME, XPATH, CSS, CLASS_NAME, TAG_NAME, LINK_TEXT, PARTIAL_LINK_TEXT;

    public By toBy(String value) {
        return switch (this) {
            case ID -> By.id(value);
            case NAME -> By.name(value);
            case XPATH -> By.xpath(value);
            case CSS -> By.cssSelector(value);
            case CLASS_NAME -> By.className(value);
            case TAG_NAME -> By.tagName(value);
            case LINK_TEXT -> By.linkText(value);
            case PARTIAL_LINK_TEXT -> By.partialLinkText(value);
        };
    }
}
