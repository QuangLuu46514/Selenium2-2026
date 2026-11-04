package automationexercise;


import common.Utilities;
import constant.Menu;


public class GeneralPage {

	// Variables
	protected static final String _menu = "//li/a[contains(normalize-space(.), '%s')]";

	// Methods
	protected void gotoTab(Menu menu) {
		Utilities.click(Utilities.getLocatorElement(_menu, menu.getText()));
	}

	@SuppressWarnings("unchecked")
	protected <T extends GeneralPage> T gotoPage(Menu menu) {

		gotoTab(menu);

        return switch (menu) {
            case HOME -> (T) new HomePage();
            case PRODUCTS -> (T) new ProductPage();
            case SIGNUP_LOGIN -> (T) new LoginPage();
            default -> throw new RuntimeException("Unsupported menu: " + menu);
        };
	}
}
