package reference.automationexercise.testcases;

import core.driver.DriverManager;
import core.listeners.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Listeners(TestListener.class)
public class TestBase {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser, Method method) {
        LOGGER.info("========== STARTING TEST: {}#{} on BROWSER: {} ==========",
                getClass().getSimpleName(), method.getName(), browser);

        DriverManager.initDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method) {
        LOGGER.info("========== END TEST: {}#{} ==========",
                getClass().getSimpleName(), method.getName());

        DriverManager.quitDriver();
    }
}