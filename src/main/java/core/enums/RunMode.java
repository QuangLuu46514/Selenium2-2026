package core.enums;

import java.util.Arrays;

public enum RunMode {
    LOCAL, REMOTE;

    /**
     * Converts a config / TestNG parameter value such as "local"
     * into a RunMode.
     */
    public static RunMode from(String value) {
        return Arrays.stream(values())
                .filter(mode -> mode.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported run mode '" + value
                                + "'. Supported: " + Arrays.toString(values())));
    }
}
