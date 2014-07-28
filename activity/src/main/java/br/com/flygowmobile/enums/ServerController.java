package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum ServerController {
    PING("ping"),
    CONNECT("connect"),
    REGISTER_DETAILS("registerDetails"),
    INITIALIZE_VIDEO_PRODUCTS("initializeProductVideo"),
    INITIALIZE_PHOTO_PRODUCTS("initializeProductImages");

    private String name;

    ServerController(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
