package core.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {
    // ThreadLocal ensures thread safety during parallel test execution
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ExtentReports extent = ExtentManager.getInstance();

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void removeFromReport(ExtentTest test) {
        ExtentManager.getInstance().removeTest(test);
    }

    public static ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.createTest(testName, desc);
        extentTest.set(test);
        return test;
    }

    // Clean up ThreadLocal reference to avoid memory leaks
    public static void unload() {
        extentTest.remove();
    }
}
