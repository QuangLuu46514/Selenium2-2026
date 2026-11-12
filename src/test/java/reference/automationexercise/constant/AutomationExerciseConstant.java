package reference.automationexercise.constant;

import common.Random;

// Claude-AI: split from the old Constant.java - only data of the Automation Exercise site lives here
public class AutomationExerciseConstant {

	public static final String AUTOMATION_EXERCISE = "https://www.automationexercise.com/";
	public static final String FULL_NAME = "Nguyen Van Test";
	public static final String FIRST_NAME = "Nguyen";
	public static final String LAST_NAME = "Test";
	public static final String EMAIL_ADDRESS = "name_"+ System.currentTimeMillis() +"@gmail.com";
	public static final String ADDRESS = Random.randomString(1,20);
	public static final String COUNTRY = "Canada";
	public static final String CITY = "Ho Chi Minh";
	public static final String ZIP_CODE = Random.randomString(1,20);
	public static final int MOBILE_PHONE = Random.randomNumber(10);
	public static final String REVIEW_TEXT = "Very Good!!!";
	public static final int MIN_INDEX_PRODUCT = 1;
	public static final int MAX_INDEX_PRODUCT = 10;
}
