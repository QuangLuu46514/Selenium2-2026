package constant;

import common.Random;

/**
 * Fixed values of the TestArchitect demo site
 * Random test data is NOT kept here - see utils.DataFactory.
 */
public class Constant {
    /* URLs */

    public static final String GURREILLAMAIL_URL = "https://www.guerrillamail.com";
    public static final String BASE_URL = "https://demo.testarchitect.com/";

    /* Test Data */
    public static final String MAIL_DOMAIN = "pokemail.net";
    public static final int MAX_PASSWORD_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final String PASSWORD = Random.randomString(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);

    /* Configuration */
    public static final int TIMEOUT = 30;
    public static final int MAX_WAIT_TIMEOUT = 300;
}
