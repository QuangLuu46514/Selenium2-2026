package constant;

import common.Random;

// Claude-AI: Automation Exercise data was moved to reference.automationexercise.constant.AutomationExerciseConstant
public class Constant {

	public static final String GURREILLAMAIL_URL = "https://www.guerrillamail.com";

	/* Test Data */
	public static final int MAX_PASSWORD_LENGTH = 20;
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final String PASSWORD = Random.randomString(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
	/* Configuration */
	public static final int TIMEOUT = 30;
	public static final int MAX_WAIT_TIMEOUT = 300;
}
