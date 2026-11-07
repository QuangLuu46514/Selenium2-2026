package core.utils;

import core.common.constant.ReportConstants;
import core.driver.Browser;
import core.report.ExtentManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
public class ScreenshotUtils {
    private ScreenshotUtils() {
        // Prevent instantiation of utility class
    }

    /**
     * Captures a screenshot of the current browser session for the active thread.
     *
     * @param testName The name of the test case, used for naming the screenshot file.
     * @return An Optional containing the path relative to the report file (e.g., "screenshots/TestName_123.png"),
     * or empty if no browser is active or capture fails. This method never throws exceptions
     * to ensure screenshot failures do not mask actual test failures.
     */
    public static Optional<String> capture(String testName) {
        try {
            // Sanitize test name using constants
            String safeName = testName.replaceAll(
                    ReportConstants.INVALID_CHAR_REGEX,
                    ReportConstants.SAFE_CHAR_REPLACEMENT
            );

            // Build dynamic file name using constants
            String timestamp = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern(ReportConstants.SCREENSHOT_TIMESTAMP_FORMAT)
            );

            String fileName = safeName
                    + ReportConstants.SCREENSHOT_NAME_SEPARATOR
                    + Thread.currentThread().threadId()
                    + ReportConstants.SCREENSHOT_NAME_SEPARATOR
                    + timestamp
                    + ReportConstants.SCREENSHOT_FILE_EXTENSION;

            Path directory = ExtentManager.getScreenshotDirectory();
            Files.createDirectories(directory);

            Path file = directory.resolve(fileName);
            Files.write(file, Browser.takeScreenshot());

            log.info("Screenshot saved successfully: {}", file.toAbsolutePath());
            return Optional.of(ReportConstants.SCREENSHOT_OUTPUT_DIR + "/" + fileName);

        } catch (IOException | RuntimeException e) {
            log.warn("Failed to capture screenshot for test '{}': {}", testName, e.getMessage());
            return Optional.empty();
        }
    }
}
