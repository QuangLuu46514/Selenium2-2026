package core.driver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import core.config.DriverConfig;
import core.enums.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import static core.driver.BrowserOptionsFactory.buildOptions;


/**
 * Creates fully configured WebDriver instances.
 *
 * <p>Responsibility: <b>how</b> a browser is built (options, headless, remote/Grid, timeouts).
 * It does not store drivers - that is the job of {@link DriverManager}.
 *
 * <p>Selenium Manager (part of Selenium 4) finds or downloads the driver binary that matches the
 * installed browser, so the framework never configures driver paths.
 */
@Slf4j
public final class DriverFactory {

    public static WebDriver createDriver(DriverConfig driverConfig) {
        log.info("Creating {} driver (headless={}, remote={})", driverConfig.getBrowser(), driverConfig.isHeadless(),
                driverConfig.getRemoteUrl().isEmpty() ? "no" : driverConfig.getRemoteUrl());

        MutableCapabilities options = buildOptions(driverConfig);
        WebDriver driver;
        try {
            driver = switch (driverConfig.getRunMode()) {
                case LOCAL -> createLocal(driverConfig.getBrowser(), options);
                case REMOTE -> createRemote(driverConfig.getRemoteUrl(), options);
            };
        } catch (SessionNotCreatedException e) {
            throw new IllegalStateException("Cannot start " + driverConfig.getBrowser() + ". Check that the browser is installed "
                    + "and that its version is supported by Selenium " + "(" + e.getRawMessage() + ")", e);
        }

        return driver;
    }

    private static WebDriver createLocal(BrowserType browser, MutableCapabilities options) {
        if (browser == null) {
            throw new IllegalArgumentException("BrowserType cannot be null. Please specify a browser.");
        }

        return switch (browser) {
            case CHROME -> new ChromeDriver((ChromeOptions) options);
            case FIREFOX -> new FirefoxDriver((FirefoxOptions) options);
            case EDGE -> new EdgeDriver((EdgeOptions) options);
        };
    }

    private static WebDriver createRemote(String remoteUrl, MutableCapabilities options) {
        try {
            URL url = URI.create(remoteUrl).toURL();
            return new RemoteWebDriver(url, options);
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new IllegalStateException("Invalid remote.url: " + remoteUrl, e);
        }
    }

    private DriverFactory() {
        // Prevent instantiation.
    }
}

