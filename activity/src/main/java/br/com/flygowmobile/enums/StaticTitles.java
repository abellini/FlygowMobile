package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum StaticTitles {
    ERROR("Error!"),
    WARNING("Warning!"),
    LOAD("Loading..."),
    SUCCESS("Successful!"),
    PROMOTIONS("Promotions")
    ;

    private String name;

    StaticTitles(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
