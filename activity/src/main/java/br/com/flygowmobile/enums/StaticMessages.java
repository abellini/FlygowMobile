package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum StaticMessages {
    TIMEOUT("Timeout Call! Please, verify the server!"),
    NOT_SERVICE("Without Service!"),
    WAIT("Please, wait. Application loading data..."),
    CALL_CONFIGURATION_FROM_SERVER("Loading default settings previously...")
    ;

    private String name;

    StaticMessages(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
