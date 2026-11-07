package core.report;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

/**
 * Utility class to simplify logging test steps, statuses, and details into ExtentReports.
 * Manages thread-safe hierarchical step nodes for parallel test execution.
 */

public class ReportLog {

    // ThreadLocal tracks the current active step node for the executing thread
    private static final ThreadLocal<ExtentTest> CURRENT_STEP_NODE = new ThreadLocal<>();

    /**
     * Executes a block of code inside a hierarchical major step (Node)
     * without duplicating logs and automatically handles context cleanup.
     *
     * @param stepName The name of the major step.
     * @param action   The action block (Lambda) to execute.
     */
    public static void step(String stepName, Runnable action) {
        ExtentTest parentTest = ExtentTestManager.getTest();
        if (parentTest == null) {
            action.run();
            return;
        }

        // Create a sub-node under the current test
        ExtentTest stepNode = parentTest.createNode(stepName);
        CURRENT_STEP_NODE.set(stepNode);

        try {
            action.run();
            // Automatically mark the node as passed if no exception occurs
            stepNode.pass(MarkupHelper.createLabel("STEP PASSED", ExtentColor.GREEN));
        } catch (Throwable e) {
            stepNode.fail(MarkupHelper.createLabel("STEP FAILED: " + stepName, ExtentColor.RED));
            stepNode.fail(e);
            throw e;
        } finally {
            CURRENT_STEP_NODE.remove(); // Clear ThreadLocal context
        }
    }

    /**
     * Determines the active logging target (prefers current step node, falls back to main test).
     */
    private static ExtentTest getActiveTarget() {
        return CURRENT_STEP_NODE.get() != null ? CURRENT_STEP_NODE.get() : ExtentTestManager.getTest();
    }

    private ReportLog() {
        // Prevent instantiation of utility class
    }
}
