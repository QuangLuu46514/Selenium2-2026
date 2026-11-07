package core.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import core.common.constant.ReportConstants;
import core.report.ExtentManager;
import core.report.ExtentTestManager;
import core.utils.ScreenshotUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Optional;

@Slf4j
public class TestListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        log.info("===== Suite '{}' started (parallel={}, threads={}) =====",
                suite.getName(), suite.getXmlSuite().getParallel(), suite.getXmlSuite().getThreadCount());
        ExtentManager.getInstance();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flush();
        log.info("===== Suite '{}' finished. Report generated at: {} =====", suite.getName(),
                ExtentManager.getRunDirectory().resolve(ReportConstants.REPORT_FILE_NAME).toAbsolutePath());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = buildTestName(result);
        String description = result.getMethod().getDescription();

        ExtentTest test = ExtentTestManager.startTest(name, description == null ? "" : description);
        assignCategories(test, result);

        test.info("Executing on Thread: " + Thread.currentThread().getName());
        log.info(">>> START TEST: {}", name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.pass(MarkupHelper.createLabel("PASSED", ExtentColor.GREEN));
        }
        log.info("<<< PASS TEST: {} (Duration: {} ms)", buildTestName(result), duration(result));
        ExtentTestManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test == null) {
            test = ExtentTestManager.startTest(buildTestName(result), "Failed during configuration or execution");
            assignCategories(test, result);
        }
        test.fail(result.getThrowable());

        // Automatically capture and attach a screenshot on test failure
        Optional<String> screenshot = ScreenshotUtils.capture(buildTestName(result));
        if (screenshot.isPresent()) {
            test.fail("Failure Screenshot",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshot.get()).build());
        }

        log.error("<<< FAIL TEST: {} - Reason: {}", buildTestName(result),
                result.getThrowable() == null ? "Unknown error" : result.getThrowable().getMessage());
        ExtentTestManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();

        if (result.wasRetried()) {
            // Keep only the final attempt in the report if the failed attempt is configured to retry
            log.warn("--- RETRY TEST: {} (Previous attempt failed: {})", buildTestName(result),
                    result.getThrowable() == null ? "" : result.getThrowable().getMessage());
            if (test != null) {
                ExtentTestManager.removeFromReport(test);
            }
        } else {
            if (test == null) {
                // Handled if skipped before onTestStart (e.g., due to dependency failure or @BeforeMethod failure)
                test = ExtentTestManager.startTest(buildTestName(result), "Skipped before execution start");
                assignCategories(test, result);
            }
            test.skip(result.getThrowable() == null ? "Test skipped" : result.getThrowable().getMessage());
            log.warn("<<< SKIP TEST: {}", buildTestName(result));
        }
        ExtentTestManager.unload();
    }

    private void assignCategories(ExtentTest test, ITestResult result) {
        String browser = null;
        if (result.getTestContext() != null && result.getTestContext().getCurrentXmlTest() != null) {
            browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        }
        if (browser != null && !browser.isEmpty()) {
            test.assignCategory(browser);
        }
        if (result.getMethod().getGroups() != null && result.getMethod().getGroups().length > 0) {
            test.assignCategory(result.getMethod().getGroups());
        }
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
    }

    public String buildTestName(ITestResult result) {
        String methodName = result.getMethod().getMethodName();

        String browser = null;
        if (result.getTestContext() != null && result.getTestContext().getCurrentXmlTest() != null) {
            browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        }

        if (browser != null && !browser.isEmpty()) {
            return methodName + " [" + browser.toUpperCase() + "]";
        }
        return methodName;
    }

    /**
     * Calculates the execution duration of a test method in milliseconds.
     */
    private static long duration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
