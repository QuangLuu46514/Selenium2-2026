package automationexercise;

import constant.Constant;
import constant.Menu;
import core.driver.Browser;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationAddressTest extends TestBase{
	HomePage homePage = new HomePage();
	LoginPage loginPage = new LoginPage();
	SignUpPage signUpPage = new SignUpPage();
	ViewCartPage viewCartPage = new ViewCartPage();
	CheckoutPage checkoutPage = new CheckoutPage();
	
	@Test
	public void TC02() {

		loginPage = homePage.open().gotoPage(Menu.SIGNUP_LOGIN);
		Browser.refresh();
		signUpPage = loginPage.signUp(Constant.FULL_NAME, Constant.EMAIL_ADDRESS);
		homePage = signUpPage.creatNewAccount(Constant.PASSWORD, Constant.FIRST_NAME, Constant.LAST_NAME, Constant.ADDRESS, Constant.COUNTRY, Constant.CITY, Constant.ZIP_CODE, Constant.MOBILE_PHONE);
		homePage.addProductToCart();
		viewCartPage = homePage.clickViewCart();
		viewCartPage.clickProceedToCheckout();
		Assert.assertEquals(checkoutPage.getDeliveryAddress(), Constant.ADDRESS, "The address is not matched");
	}

}
