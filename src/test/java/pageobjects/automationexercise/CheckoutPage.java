package automationexercise;

import org.openqa.selenium.By;

import common.Utilities;

public class CheckoutPage extends GeneralPage{

	// Locators
	private static final By _deliveryAddress = By.xpath("//ul[@id='address_delivery']//li[contains(@class, 'address_address2') and normalize-space(.) != '']");

	// Methods
	public String getDeliveryAddress() {
		return Utilities.getText(_deliveryAddress);
	}
	
}

