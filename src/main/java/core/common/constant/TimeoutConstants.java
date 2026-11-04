package core.common.constant;

import java.time.Duration;

/**
 * Centralized timeout constants for the automation framework.
 */
public final class TimeoutConstants {

    /** Maximum time to wait for a page to finish loading. */
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);

    /** Default timeout for UI actions and explicit waits. */
    public static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    /** Polling interval used by explicit waits. */
    public static final Duration POLLING_INTERVAL = Duration.ofMillis(250);

    private TimeoutConstants() {
        // Prevent instantiation.
    }
}
