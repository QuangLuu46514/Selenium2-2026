package utils;

/**
 * Creates NEW test data on every call.
 *
 * <p>This is the opposite of "static final" data in a Constant class, which is created only once
 * for the whole run. With parallel tests every test must get its own email, otherwise two tests
 * try to register the same account.
 */

public class DataFactory {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTERS_AND_DIGITS = LETTERS + "0123456789";


    /**
     * Example: qluuknguyen_1731345678901@pokemail.net
     */
    public static String randomEmail() {
    return "qluuknguyen" + System.currentTimeMillis() + "@";
    }
}
