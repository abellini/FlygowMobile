package br.com.flygowmobile.Utils;

import android.app.Application;
import android.content.Context;

import br.com.flygowmobile.enums.ServerController;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public class App extends Application {
    private static String HTTP_HEADER = "http://";
    private static String APPLICATION_NAME = "flygow";
    private static String WEBSERVICE_PREFIX = "webservice";
    private static String PORT_SEPARATOR = ":";
    private static String PATH_SEPARATOR = "/";

    private static Context mContext;

    private String serverIp;
    private int serverPort;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public App() {
        this.serverIp = "127.0.0.1";
        this.serverPort = 8080;
    }

    public App(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public static Context getContext(){
        return mContext;
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

    public String getServerUrl(ServerController controller) {
        return this.buildUrl(controller);
    }

    private String buildUrl(ServerController controller) {
        String url = HTTP_HEADER + serverIp + PORT_SEPARATOR + serverPort + PATH_SEPARATOR +
                APPLICATION_NAME + PATH_SEPARATOR + WEBSERVICE_PREFIX + PATH_SEPARATOR + controller.getName();
        return url;
    }
}
