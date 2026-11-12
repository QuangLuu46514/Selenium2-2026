package reference.automationexercise.pageobjects;

import org.openqa.selenium.By;

import common.Utilities;

public class ProductDetailsPage extends GeneralPage {

	// Locators
	private static final By _lblReview = By.xpath("//li[@class='active']/a");
	private static final By _txtName = By.xpath("//input[@id='name']");
	private static final By _txtEmail = By.xpath("//input[@id='email']");
	private static final By _txtReview = By.xpath("//textarea[@id='review']");
	private static final By _btnSubmit = By.xpath("//button[@id='button-review']");
	private static final By _lblReviewSuccessMessage = By.xpath("//div[contains(@class,'alert-success')]//span");

	// Methods

	public void enterName(String name) {
		Utilities.enter(_txtName, name);
	}

	public void enterEmail(String email) {
		Utilities.enter(_txtEmail, email);
	}

	public void enterReviewText(String reviewText) {
		Utilities.enter(_txtReview, reviewText);
	}

	public void clickSubmit() {
		Utilities.click(_btnSubmit);
	}

	public void fillReviewInfo(String name, String email, String reviewText) {
		enterName(name);
		enterEmail(email);
		enterReviewText(reviewText);
		clickSubmit();
	}
	
	public boolean isWriteYourReviewDisplay() {
        return Utilities.findElement(_lblReview).isDisplayed()
                && Utilities.findElement(_txtName).isDisplayed()
                && Utilities.findElement(_txtEmail).isDisplayed()
                && Utilities.findElement(_txtReview).isDisplayed();
    }
	
	public boolean isReviewSuccessMessageDisplay() {
		return Utilities.findElement(_lblReviewSuccessMessage).isDisplayed();
	}
}
