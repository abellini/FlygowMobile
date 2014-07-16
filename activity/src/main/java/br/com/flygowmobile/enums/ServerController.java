package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum ServerController {
    PING("ping"),
    CONNECT("connect"),
    REGISTER_DETAILS("registerDetails"),
    INITIALIZE_MEDIA_ADVERTISEMENTS("initializeMediaAdvertisements"),
    INITIALIZE_PHOTO_FOODS("initializeFoodImages");

    private String name;

    ServerController(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
