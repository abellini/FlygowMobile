package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum StaticMessages {
    TIMEOUT("ping"),
    NOT_SERVICE("connect"),
    WAIT("Wait...")
    ;

    private String name;

    StaticMessages(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
