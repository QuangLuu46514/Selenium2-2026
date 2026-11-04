package core.enums;

import java.util.Arrays;

public enum BrowserType {
    CHROME, FIREFOX, EDGE;

    /** Converts a config / TestNG parameter value such as "chrome" into a BrowserType. */
    public static BrowserType from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported browser '" + value + "'. Supported: " + Arrays.toString(values())));
    }
}
