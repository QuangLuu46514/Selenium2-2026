package reference.automationexercise.pageobjects;

import common.Utilities;

public class ProductPage extends GeneralPage{
	
	// Variable
	private static final String _btnViewProduct = "//a[@href='/product_details/%s']";
	
	// Methods
	public void clickViewProduct(String index) {
		Utilities.click(Utilities.getLocatorElement(_btnViewProduct, index));
	}
	
	public void selectProduct(int indexRandom) {
		String indexStr = String.valueOf(indexRandom);
		clickViewProduct(indexStr);
	}

}
