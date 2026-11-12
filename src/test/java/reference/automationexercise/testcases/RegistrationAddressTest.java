package reference.automationexercise.testcases;

import constant.Constant;
import reference.automationexercise.constant.Menu;
import reference.automationexercise.constant.AutomationExerciseConstant;
import core.driver.Browser;
import org.testng.Assert;
import org.testng.annotations.Test;
import reference.automationexercise.pageobjects.*;

public class RegistrationAddressTest extends TestBase {
	HomePage homePage = new HomePage();
	LoginPage loginPage = new LoginPage();
	SignUpPage signUpPage = new SignUpPage();
	ViewCartPage viewCartPage = new ViewCartPage();
	CheckoutPage checkoutPage = new CheckoutPage();
	
	@Test
	public void TC02() {

		loginPage = homePage.open().gotoPage(Menu.SIGNUP_LOGIN);
		Browser.refresh();
		signUpPage = loginPage.signUp(AutomationExerciseConstant.FULL_NAME, AutomationExerciseConstant.EMAIL_ADDRESS);
		homePage = signUpPage.creatNewAccount(Constant.PASSWORD, AutomationExerciseConstant.FIRST_NAME, AutomationExerciseConstant.LAST_NAME, AutomationExerciseConstant.ADDRESS, AutomationExerciseConstant.COUNTRY, AutomationExerciseConstant.CITY, AutomationExerciseConstant.ZIP_CODE, AutomationExerciseConstant.MOBILE_PHONE);
		homePage.addProductToCart();
		viewCartPage = homePage.clickViewCart();
		viewCartPage.clickProceedToCheckout();
		Assert.assertEquals(checkoutPage.getDeliveryAddress(), AutomationExerciseConstant.ADDRESS, "The address is not matched");
	}

}
