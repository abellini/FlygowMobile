package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum ServerController {
    CONNECT("connect"),
    REGISTER_DETAILS("registerDetails")
    ;

    private String name;

    ServerController(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
