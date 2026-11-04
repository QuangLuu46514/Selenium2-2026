package automationexercise;

import org.testng.annotations.Test;

public class ProductReviewTest extends TestBase {
	HomePage homePage = new HomePage();

	@Test
	public void TC01() {
		homePage.open();
	}
}
