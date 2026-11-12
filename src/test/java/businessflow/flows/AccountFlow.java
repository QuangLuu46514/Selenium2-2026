package flows;

import models.Account;
import utils.DataFactory;

/**
 * Account steps that many tests need.
 * A "business flow" joins steps of several pages into one action with a business meaning,
 * so a test can call registerNewAccount() instead of repeating 4 lines.
 */
public class AccountFlow {
    /**
     * Opens the site and registers a brand new account. The site logs the new user in right away,
     * so when this method returns, the browser is on My Account, logged in.
     */

    public static Account registerNewAccount() {
        String email = DataFactory.randomEmail();
    }
}
