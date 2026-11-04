package constant;

public enum CreateAccountFields {
	PASSWORD("password"),
	FIRST_NAME("first_name"),
	LAST_NAME("last_name"),
	ADDRESS("address1"),
	COUNTRY("country"),
	STATE("state"),
	CITY("city"),
	ZIPCODE("zipcode"),
	MOBILE_NUMBER("mobile_number");
	
	private final String nameFiled;

	CreateAccountFields(String nameFiled) {
		this.nameFiled = nameFiled;
	}

	public String getText() {
		return nameFiled;
	}
}
