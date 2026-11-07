package railway;

import org.testng.annotations.Test;
import pageobjects.automationexercise.railway.HomePage;

public class ProductReviewTest extends TestBase {
	HomePage homePage = new HomePage();

	@Test
	public void TC01() {
		homePage.open();
	}
}
