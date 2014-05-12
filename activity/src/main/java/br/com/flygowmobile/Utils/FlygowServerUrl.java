package br.com.flygowmobile.Utils;

import android.app.Application;

import br.com.flygowmobile.enums.ServerController;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public class FlygowServerUrl extends Application{
    private static String HTTP_HEADER = "http://";
    private static String APPLICATION_NAME = "flygow";
    private static String WEBSERVICE_PREFIX = "webservice";
    private static String PORT_SEPARATOR = ":";
    private static String PATH_SEPARATOR = "/";

    private String serverIp;
    private int serverPort;

    public FlygowServerUrl(){
        this.serverIp = "127.0.0.1";
        this.serverPort = 8080;
    }

    public FlygowServerUrl(String serverIp, int serverPort){
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerUrl(ServerController controller){
        return this.buildUrl(controller);
    }

    private String buildUrl(ServerController controller){
        String url = HTTP_HEADER + serverIp + PORT_SEPARATOR + serverPort + PATH_SEPARATOR +
                APPLICATION_NAME + PATH_SEPARATOR + WEBSERVICE_PREFIX + PATH_SEPARATOR + controller.getName();
        return url;
    }
}
