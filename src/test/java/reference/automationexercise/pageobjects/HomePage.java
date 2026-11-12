package reference.automationexercise.pageobjects;

import core.driver.Browser;
import org.openqa.selenium.By;

import common.Random;
import common.Utilities;
import reference.automationexercise.constant.AutomationExerciseConstant;

public class HomePage extends GeneralPage{

	// String
	private static final String _productXpath = "//div[contains(@class,'productinfo')]//a[@data-product-id='%s']";

    // Locators
	private static final By _btnViewCart = By.xpath("//div[contains(@class, 'modal-confirm')]//a[@href='/view_cart']");

	// Methods
	public HomePage open() {
		Browser.navigateTo(AutomationExerciseConstant.AUTOMATION_EXERCISE);
		return this;
	}
	
	public void addProductToCart() {
		int indexRandom = Random.randomNumber(AutomationExerciseConstant.MIN_INDEX_PRODUCT, AutomationExerciseConstant.MAX_INDEX_PRODUCT);
		String indexStr = String.valueOf(indexRandom);
        // Variables
        Utilities.hover(Utilities.getLocatorElement(_productXpath, indexStr));
		Utilities.click(Utilities.getLocatorElement(_productXpath, indexStr));
	}
	
	public ViewCartPage clickViewCart() {
		Utilities.click(_btnViewCart);
		return new ViewCartPage();
	}
	
	
}
