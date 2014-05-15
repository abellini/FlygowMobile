package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum StaticMessages {
    TIMEOUT("Timeout Call! Please, verify the server!"),
    NOT_SERVICE("Without Service!"),
    WAIT("Please, wait. Application loading data..."),
    LOCAL_LOAD("Loading data into local database..."),
    REGISTER_FROM_SERVER("Please, wait while register data into server!"),
    SUCCESS_SAVE_IN_SERVER("Data saved with success in server!"),
    CALL_CONFIGURATION_FROM_SERVER("Loading default settings previously..."),
    EXCEPTION("An internal tablet exception occured! Please, verify the app log!")
    ;

    private String name;

    StaticMessages(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
