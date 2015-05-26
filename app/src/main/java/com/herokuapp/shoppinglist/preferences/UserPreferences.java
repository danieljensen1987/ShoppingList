package com.herokuapp.shoppinglist.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.herokuapp.shoppinglist.models.ShoppingList;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserPreferences {

    public static final String SP_NAME = "com.herokuapp.shoppingapp.PREFS";

    SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE); // the default mode, where the created file can only be accessed by the calling application (or all applications sharing the same user ID).
    }

    public void storeUserID(String UID) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UID", UID);
        editor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getUID(){
        return preferences.getString("UID", null); // (string key, string default)
    }

    public boolean isLoggedIn(){
        return getUID() != null;
    }
}
