package reference.automationexercise.testcases;

import org.testng.annotations.Test;
import reference.automationexercise.pageobjects.HomePage;

public class ProductReviewTest extends TestBase {
	HomePage homePage = new HomePage();

	@Test
	public void TC01() {
		homePage.open();
	}
}
