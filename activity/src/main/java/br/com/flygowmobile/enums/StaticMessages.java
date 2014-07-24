package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum StaticMessages {
    TIMEOUT("Timeout Call! Please, ver ify the server!"),
    NOT_SERVICE("Without Service!"),
    WAIT("Please, wait. Application loading data..."),
    LOCAL_LOAD("Getting data from server to the tablet database..."),
    REGISTER_FROM_SERVER("Please, wait while register data into server!"),
    SUCCESS_SAVE_IN_SERVER("Data saved with success in server!"),
    CALL_CONFIGURATION_FROM_SERVER("Loading default settings previously..."),
    EXCEPTION("An internal tablet exception occured! Please, verify the app log!"),
    MISSING_CONFIGURATIONS("Currently, the settings that are NOT loaded are: \n"),
    COIN_CONFIGURATION("Coin Configuration (*)"),
    ATTENDANT_CONFIGURATION("Attendant Configuration (*)"),
    ADVERTISEMENT_CONFIGURATION("Advertisement Configuration"),
    MANDATORY_CONFIGURATIONS("(*) Mandatory Configurations."),
    DEFINE_CHOICES("Default values ​​are displayed. Please set your choices to proceed!"),
    MORE_DETAILS("+ details"),
    LOADING_ADVERTISEMENTS("Loading advertisements from server..."),
    LOADING_PHOTO_PRODUCTS("Loading product photos from server..."),
    LOAD_PRODUCT("Load product information..."),
    WARNING_LOAD_PHOTO("Some photos of products are not loaded from the server. Please, verify!"),
    WARNING_LOAD_ADVERTISEMENTS("Some advertisements are not loaded from the server. Please, verify!"),
    GENERIC("");

    private String name;

    StaticMessages(String name) {
        this.name = name;
    }

    public static StaticMessages getCustomMessage(String message) {
        GENERIC.setName(message);
        return StaticMessages.GENERIC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
