package automationexercise;

import org.openqa.selenium.By;

import common.Utilities;
import constant.CreateAccountFields;

public class SignUpPage extends GeneralPage {

	// Variables
	private static final String _txtFields = "//input[@id='%s']";
	private static final String _country = "//select[@id='%s']";

	// Locators
	private static final By _btnCreateAccount = By.xpath("//button[contains(text(), 'Create Account')]");

	// Methods
	
	public HomePage creatNewAccount(String password, 
			String firstName, String lastName, String 
			address, String country, String city, String zipCode, 
			int mobilePhone) {
		Utilities.enter(Utilities.getLocatorElement(_txtFields, CreateAccountFields.PASSWORD.getText()), password);
		Utilities.enter(Utilities.getLocatorElement(_txtFields, CreateAccountFields.FIRST_NAME.getText()), firstName);
		Utilities.enter(Utilities.getLocatorElement(_txtFields, CreateAccountFields.LAST_NAME.getText()), lastName);
		Utilities.enter(Utilities.getLocatorElement(_txtFields, CreateAccountFields.ADDRESS.getText()), address);
		Utilities.enter(Utilities.getLocatorElement(_country, CreateAccountFields.COUNTRY.getText()), country);
		Utilities.enter(Utilities.getLocatorElement(_txtFields, CreateAccountFields.CITY.getText()), city);
		Utilities.enter(Utilities.getLocatorElement(_txtFields, CreateAccountFields.ZIPCODE.getText()), zipCode);
		Utilities.enter(Utilities.getLocatorElement(_txtFields, CreateAccountFields.MOBILE_NUMBER.getText()), String.valueOf(mobilePhone));
		Utilities.click(_btnCreateAccount);
		return new HomePage();
	}
	
}
