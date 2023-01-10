package com.example.bookinder;

import android.app.Application;

public class OtherUser extends Application {

    public static String otherUser;
    public static String name;

    public static String getOtherUser() {
        return otherUser;
    }

    public static void setOtherUser(String someVariable) {
        otherUser = someVariable;
    }
    public static void setOtherName(String someVariable){
        name = someVariable;
    }

}
