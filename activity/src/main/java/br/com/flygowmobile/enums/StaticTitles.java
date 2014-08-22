package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum StaticTitles {
    ERROR("Error!"),
    WARNING("Warning!"),
    LOAD("Loading..."),
    SUCCESS("Successful!"),
    PROMOTIONS("Promotions"),
    INFORMATION("Information"),
    PROMOTION_INFO("The items included in this promotion are:"),
    MENU("Menu"),
    SUBTITLE_MENU("Choose any menu options above!"),
    MAKE_ORDER("Complete your order!"),
    ACCOMPANIMENT_POPUP("Choose yours accompaniments!"),
    CONTINUE("Continue"),
    CANCEL("Cancel"),
    QUANTITY_CHOICES("Choice the Quantity"),
    ENTER_OBSERVATIONS("Enter with Observations"),
    MAIN_APP_TITLE("Flygow - Digital Menu"),
    YOUR_ACCOUNT("Your Account");
    ;

    private String name;

    StaticTitles(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
