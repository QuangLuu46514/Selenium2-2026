package core.driver;

import core.config.DriverConfig;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class BrowserOptionsFactory {

    public static MutableCapabilities buildOptions(DriverConfig config) {
        return switch (config.getBrowser()) {
            case CHROME -> buildChromeOptions(config);
            case FIREFOX -> buildFirefoxOptions(config);
            case EDGE -> buildEdgeOptions(config);
            default -> throw new IllegalStateException("Unexpected value: " + config.getBrowser());
        };
    }

    private static ChromeOptions buildChromeOptions(DriverConfig config) {
        ChromeOptions options = new ChromeOptions();

        if (config.isChromeDisableNotifications()) {
            options.addArguments("--disable-notifications");
        }

        if (config.isHeadless()) {
            options.addArguments(
                    config.getChromeHeadlessArgument());
        }

        return options;
    }

    private static FirefoxOptions buildFirefoxOptions(DriverConfig config) {
        FirefoxOptions options = new FirefoxOptions();

        if (config.isFirefoxDisableNotifications()) {
            options.addPreference(
                    "dom.webnotifications.enabled", false);
        }

        if (config.isHeadless()) {
            options.addArguments(
                    config.getFirefoxHeadlessArgument());
        }

        return options;
    }

    private static EdgeOptions buildEdgeOptions(DriverConfig config) {
        EdgeOptions options = new EdgeOptions();

        if (config.isEdgeDisableNotifications()) {
            options.addArguments("--disable-notifications");
        }

        if (config.isHeadless()) {
            options.addArguments(
                    config.getEdgeHeadlessArgument());
        }

        return options;
    }

    private BrowserOptionsFactory() {
        // Prevent instantiation.
    }
}

