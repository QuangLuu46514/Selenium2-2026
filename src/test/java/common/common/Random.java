package common;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public static String randomString(int min, int max) {
		int length = RANDOM.nextInt(max - min + 1) + min;
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
		}
		return sb.toString();
	}

	public static int randomNumber(int max) {
	    return RANDOM.nextInt(max);
	}

	public static int randomNumber(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

}
