package core.config;

import core.enums.BrowserType;
import core.enums.DriverKey;
import core.enums.RunMode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public final class DriverConfig {

    private static final String CONFIG_FILE = "config.properties";

    private final RunMode runMode;
    private final String remoteUrl;
    @Setter
    private BrowserType browser;
    private final boolean headless;

    private final boolean chromeDisableNotifications;
    private final String chromeHeadlessArgument;

    private final boolean firefoxDisableNotifications;
    private final String firefoxHeadlessArgument;

    private final boolean edgeDisableNotifications;
    private final String edgeHeadlessArgument;

    public DriverConfig() {
        Properties properties = loadProperties();

        this.remoteUrl = getProperty(properties, DriverKey.RUN_REMOTE_URL.getKey(), "");

        this.runMode = RunMode.from(
                getProperty(properties, DriverKey.RUN_MODE.getKey(), "local"));

        this.browser = BrowserType.from(getProperty(properties, DriverKey.BROWSER.getKey(), "chrome"));

        this.headless = Boolean.parseBoolean(
                getProperty(properties, DriverKey.BROWSER_HEADLESS.getKey(), "true"));

        this.chromeDisableNotifications = Boolean.parseBoolean(
                getProperty(properties,
                        DriverKey.CHROME_DISABLE_NOTIFICATIONS.getKey(), "true"));

        this.chromeHeadlessArgument = getProperty(
                properties, DriverKey.CHROME_HEADLESS_ARGUMENT.getKey(), "--headless=new");

        this.firefoxDisableNotifications = Boolean.parseBoolean(
                getProperty(properties,
                        DriverKey.FIREFOX_DISABLE_NOTIFICATIONS.getKey(), "true"));

        this.firefoxHeadlessArgument = getProperty(
                properties, DriverKey.FIREFOX_HEADLESS_ARGUMENT.getKey(), "-headless");

        this.edgeDisableNotifications = Boolean.parseBoolean(
                getProperty(properties,
                        DriverKey.EDGE_DISABLE_NOTIFICATIONS.getKey(), "true"));

        this.edgeHeadlessArgument = getProperty(
                properties, DriverKey.EDGE_HEADLESS_ARGUMENT.getKey(), "--headless=new");
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new IllegalStateException(
                        "Configuration file not found: " + CONFIG_FILE);
            }

            properties.load(input);
            return properties;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load " + CONFIG_FILE, e);
        }
    }

    private static String getProperty(Properties properties, String key, String defaultValue) {
        String value = properties.getProperty(key);

        return value == null || value.isBlank()
                ? defaultValue
                : value.trim();
    }

}
