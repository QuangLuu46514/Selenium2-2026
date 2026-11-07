package core.common.constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportConstants {

    // ---- Paths and File Names ----
    public static final String REPORT_OUTPUT_DIR = "reports";
    public static final String SCREENSHOT_OUTPUT_DIR = "screenshots";

    private static final String RUN_FOLDER_NAME = "run_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

    public static String getRunFolder() {
        return RUN_FOLDER_NAME;
    }

    public static final String REPORT_FILE_NAME = "extent-report.html";

    public static String getReportFilePath() {
        return REPORT_OUTPUT_DIR + "/" + RUN_FOLDER_NAME + "/" + REPORT_FILE_NAME;
    }

    public static final String REPORT_NAME = "Selenium Automation Execution Report";
    public static final String DOCUMENT_TITLE = "Automation Test Execution Results";

    // ---- Screenshot Formatting Constants ----
    public static final String SCREENSHOT_FILE_EXTENSION = ".png";
    public static final String SCREENSHOT_TIMESTAMP_FORMAT = "HHmmssSSS";
    public static final String SCREENSHOT_NAME_SEPARATOR = "_";
    public static final String INVALID_CHAR_REGEX = "[^a-zA-Z0-9._-]";
    public static final String SAFE_CHAR_REPLACEMENT = "_";

    // ---- System Info Keys ----
    public static final String SYS_INFO_FRAMEWORK = "Framework";
    public static final String SYS_INFO_FRAMEWORK_VALUE = "Selenium WebDriver";
    public static final String SYS_INFO_LANGUAGE = "Language";
    public static final String SYS_INFO_LANGUAGE_VALUE = "Java";


    private ReportConstants() {
        // Prevent instantiation of utility class
    }
}
