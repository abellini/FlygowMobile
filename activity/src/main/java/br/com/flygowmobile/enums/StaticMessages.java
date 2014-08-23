package br.com.flygowmobile.enums;

import br.com.flygowmobile.activity.R;

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
    LOADING_MEDIA_PRODUCTS("Loading product medias from server..."),
    LOADING_MEDIA_PROMOTIONS("Loading promotion medias from server..."),
    LOAD_PRODUCT("Load product information..."),
    LOAD_PROMOTION("Load promotion information..."),
    WARNING_LOAD_PRODUCTS("Some medias of products are not loaded from the server. Please, verify!"),
    WARNING_LOAD_PROMOTIONS("Some medias of promotions are not loaded from the server. Please, verify!"),
    WARNING_LOAD_ADVERTISEMENTS("Some medias of advertisements are not loaded from the server. Please, verify!"),
    FREE("Free"),
    MAXIMUM_OF("Maximum of"),
    OPTIONS("options"),
    QUANTITY_SUBTILE("Use the buttons to define the quantity of products in this order"),
    OBSERVATIONS_SUBTITLE("Describe your observations about this order"),
    OBSERVATIONS_EMPTY_TEXT("Click here and enter your observations..."),
    NO_OBSERVATIONS("No Observations"),
    DEFAULT_QTD_ORDER("01"),
    LOAD_ORDER_ITEM("Adding order item..."),
    CALLING_ATTENDANT("Calling attendant..."),
    CONFIRM_CALL_ATTENDANT("Click in 'Yes' to call the attendant."),
    CALL_ATTENDANT("The attendant was called. Wait, it will come to his table."),
    CALL_ATTENDANT_ERROR("There was an error call attendant. Please call it the conventional way."),
    GENERIC("");

    private String name;
    private int resId;

    StaticMessages(String name) {
        this.name = name;
    }

    StaticMessages(int resId) {
        this.resId = resId;
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
