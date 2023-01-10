package com.example.bookinder;

import android.app.Application;

public class CurrentUser extends Application {

    public static String currentUser;
    public static String name;

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String someVariable) {
        currentUser = someVariable;
    }
    public void setName(String someVariable) {name =someVariable;}
}
