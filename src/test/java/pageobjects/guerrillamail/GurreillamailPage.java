package guerrillamail;

import common.Utilities;
import common.constant.Constant;
import common.constant.MailType;
import core.driver.Browser;
import org.openqa.selenium.By;

public class GurreillamailPage {

    // Locators
    private final By _btnEdit = By.xpath("//span[@id='inbox-id']");
    private final By _txtEmail = By.xpath("//span[@id='inbox-id']//input");
    private final By _btnSet = By.xpath("//button[@class='save button small']");
    private final By _linkConfirmEmail = By.xpath("//div[@class='email_body']//a");

    // Methods
    public void setAnEmail(String email) {
        Utilities.click(_btnEdit);
        Utilities.enter(_txtEmail, email);
        Utilities.click(_btnSet);
    }

    public void waitAndClickConfirmEmail(MailType mailType) {
        Browser.refresh();
        // Variables
        String _lblMailType = "//tbody[@id='email_list']/tr//td[contains(normalize-space(), '%s')]";
        By mailLocator = By.xpath(String.format(_lblMailType, mailType.getText()));

        long endTime = System.currentTimeMillis() + (Constant.MAX_WAIT_TIMEOUT * 1000);

        while (System.currentTimeMillis() < endTime) {

            Browser.refresh();

            if (!Utilities.findElements(mailLocator).isEmpty()) {
                Utilities.click(mailLocator);
                break;
            }
        }

        Utilities.waitForVisible(_linkConfirmEmail);
        Utilities.findElement(_linkConfirmEmail).click();
    }

    public void activeAccount(String email) {
        String mailName = email.split("@")[0];
        Browser.openNewTab(Constant.GURREILLAMAIL_URL);
        setAnEmail(mailName);
        waitAndClickConfirmEmail(MailType.CONFIRM_ACCOUNT);
        Browser.switchToLastWindow();
    }

    public void confirmResetPassword() {
        Browser.openNewTab(Constant.GURREILLAMAIL_URL);
        waitAndClickConfirmEmail(MailType.RESET_PASSWORD);
        Browser.switchToLastWindow();
    }

}
