package constant;

public enum MailType {
    CONFIRM_ACCOUNT("Please confirm your account"), RESET_PASSWORD("Please reset your password");

    private final String displayText;

    MailType(String displayText) {
        this.displayText = displayText;
    }

    public String getText() {
        return displayText;
    }
}
