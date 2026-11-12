package reference.automationexercise.constant;

public enum Menu {
	HOME("Home"),
	PRODUCTS("Products"),
	SIGNUP_LOGIN("Signup / Login");

    private final String nameMenu;

    Menu(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    public String getText() {
        return nameMenu;
    }
	
}
