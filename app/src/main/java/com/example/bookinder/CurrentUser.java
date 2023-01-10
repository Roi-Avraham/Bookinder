package com.example.bookinder;

import android.app.Application;

public class CurrentUser extends Application {

    public static String currentUser;
    public static String name;
    public static String profile_picture;

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String someVariable) {
        currentUser = someVariable;
    }
    public static void setName(String someVariable) {name =someVariable;}
    public static void setProfile_picture(String someVariable) {profile_picture =someVariable;}
}
