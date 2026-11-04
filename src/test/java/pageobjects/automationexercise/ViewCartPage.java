package automationexercise;

import org.openqa.selenium.By;

import common.Utilities;

public class ViewCartPage extends GeneralPage{
	
	// Locators
	private static final By _btnProceedToCheckout = By.xpath("//a[contains(@class, 'check_out')]");
	
	
	// Methods
	public void clickProceedToCheckout() {
		Utilities.click(_btnProceedToCheckout);
	}
}
