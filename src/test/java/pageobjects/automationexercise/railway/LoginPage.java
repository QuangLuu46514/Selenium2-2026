package pageobjects.automationexercise.railway;

import org.openqa.selenium.By;

import common.Utilities;

public class LoginPage extends GeneralPage {

	// Locators
	private static final By _txtName = By.xpath("//form[@action='/signup']//input[@name='name']");
	private static final By _txtEmail = By.xpath("//form[@action='/signup']//input[@name='email']");
	private static final By _btnSignUp = By.xpath("//form[@action='/signup']//button[contains(text(), 'Signup')]");

	// Methods
	public SignUpPage signUp(String name, String email) {
		Utilities.enter(_txtName, name);
		Utilities.enter(_txtEmail, email);
		Utilities.click(_btnSignUp);
		return new SignUpPage();
	}
}
