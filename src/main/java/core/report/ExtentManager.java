package core.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.common.constant.ReportConstants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentManager {
    private static ExtentReports extentReports;

    public static Path getRunDirectory() {
        return Paths.get(ReportConstants.REPORT_OUTPUT_DIR, ReportConstants.getRunFolder());
    }

    public static Path getScreenshotDirectory() {
        return getRunDirectory().resolve(ReportConstants.SCREENSHOT_OUTPUT_DIR);
    }

    public synchronized static ExtentReports getInstance() {
        if (extentReports == null) {
            extentReports = new ExtentReports();

            ExtentSparkReporter reporter = new ExtentSparkReporter(ReportConstants.getReportFilePath());

            reporter.config().setReportName(ReportConstants.REPORT_NAME);
            reporter.config().setDocumentTitle(ReportConstants.DOCUMENT_TITLE);
            reporter.config().setTheme(Theme.STANDARD);

            extentReports.attachReporter(reporter);
            extentReports.setSystemInfo(ReportConstants.SYS_INFO_FRAMEWORK, ReportConstants.SYS_INFO_FRAMEWORK_VALUE);
            extentReports.setSystemInfo(ReportConstants.SYS_INFO_LANGUAGE, ReportConstants.SYS_INFO_LANGUAGE_VALUE);
            extentReports.setSystemInfo("OS", System.getProperty("os.name", "Unknown"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version", "Unknown"));
            extentReports.setSystemInfo("User", System.getProperty("user.name", "Unknown"));

            // Register JVM shutdown hook to guarantee report flush on abnormal exits
            Runtime.getRuntime().addShutdownHook(new Thread(ExtentManager::flush));
        }
        return extentReports;
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
