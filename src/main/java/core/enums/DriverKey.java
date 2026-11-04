package core.enums;

import lombok.Getter;

@Getter
public enum DriverKey {

    // Driver Configuration
    RUN_MODE("run.mode"),
    RUN_REMOTE_URL("run.remote.url"),

    BROWSER("browser"),
    BROWSER_HEADLESS("browser.headless"),

    // Chrome Configuration
    CHROME_DISABLE_NOTIFICATIONS(
            "browser.chrome.disable-notifications"),
    CHROME_HEADLESS_ARGUMENT(
            "browser.chrome.headless-argument"),

    // Firefox Configuration
    FIREFOX_DISABLE_NOTIFICATIONS(
            "browser.firefox.disable-notifications"),
    FIREFOX_HEADLESS_ARGUMENT(
            "browser.firefox.headless-argument"),

    // Edge Configuration
    EDGE_DISABLE_NOTIFICATIONS(
            "browser.edge.disable-notifications"),
    EDGE_HEADLESS_ARGUMENT(
            "browser.edge.headless-argument");

    private final String key;

    DriverKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}

