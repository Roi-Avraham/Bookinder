package com.example.bookinder;

import android.app.Application;

public class ServerAddress  extends Application {

    public static String serverAddress = "http://192.168.1.169:5000";

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String someVariable) {
        serverAddress = someVariable;
    }
}
