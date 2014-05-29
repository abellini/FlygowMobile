package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryTablet;

public class Tablet implements Serializable {

    public static String[] columns = new String[]{
            RepositoryTablet.Tablets.COLUMN_NAME_TABLET_ID,
            RepositoryTablet.Tablets.COLUMN_NAME_STATUS_ID,
            RepositoryTablet.Tablets.COLUMN_NAME_COIN_ID,
            RepositoryTablet.Tablets.COLUMN_NAME_NUMBER,
            RepositoryTablet.Tablets.COLUMN_NAME_IP,
            RepositoryTablet.Tablets.COLUMN_NAME_PORT,
            RepositoryTablet.Tablets.COLUMN_NAME_SERVER_IP,
            RepositoryTablet.Tablets.COLUMN_NAME_SERVER_PORT,
            RepositoryTablet.Tablets.COLUMN_NAME_ATTENDANT_ID
    };

    private long tabletId;
    private int statusId;
    private int coinId;
    private int number;
    private String ip;
    private int port;
    private String serverIP;
    private int serverPort;
    private long attendantId;


    public Tablet(long tabletId, int statusId, int coinId, int number, String ip, int port, String serverIP, int serverPort, long attendantId) {
        this.tabletId = tabletId;
        this.statusId = statusId;
        this.coinId = coinId;
        this.number = number;
        this.ip = ip;
        this.port = port;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.attendantId = attendantId;
    }

    public Tablet(int number, String ip, int port, String serverIP, int serverPort) {
        this.number = number;
        this.ip = ip;
        this.port = port;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }


    public Tablet() {
    }

    public long getTabletId() {
        return tabletId;
    }

    public void setTabletId(long tabletId) {
        this.tabletId = tabletId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getCoinId() {
        return coinId;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public long getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(long attendantId) {
        this.attendantId = attendantId;
    }

    public String toJSONInitialConfig() {
        return "{" +
                "\"number\": " + getNumber() + ", " +
                "\"ip\": " + "\"" + getIp() + "\", " +
                "\"port\": " + getPort() + ", " +
                "\"serverIP\": " + "\"" + getServerIP() + "\", " +
                "\"serverPort\": " + getServerPort() +
                "}";
    }

    public String toJSONDetailsConfig() {
        return "{" +
                "\"number\": " + getNumber() + ", " +
                "\"statusId\": " + getStatusId() + ", " +
                "\"coinId\": " + getCoinId() + ", " +
                "\"attendantId\": " + getAttendantId() +
                "}";
    }
}
