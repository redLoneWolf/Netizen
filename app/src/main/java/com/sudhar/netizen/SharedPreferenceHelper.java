package com.sudhar.netizen;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceHelper {


    private SharedPreferences preferences;

    public SharedPreferenceHelper(Context context) {
        this.preferences = context.getSharedPreferences("Netizen", context.MODE_PRIVATE);
    }

    public String getAccessToken() {
        return preferences.getString("Access", "");
    }

    public String getRefreshToken() {
        return preferences.getString("Refresh", "");
    }

    public String getUsername() {
        return preferences.getString("Username", "");
    }

    public int getUserId() {
        return preferences.getInt("UserId", 0);
    }


    public void setAccessToken(String token) {
        preferences.edit().putString("Access", token).apply();
    }

    public void setRefreshToken(String token) {
        preferences.edit().putString("Refresh", token).apply();
    }

    public void setUsername(String username) {
        preferences.edit().putString("Username", username).apply();
    }

    public void setUserId(int id) {
        preferences.edit().putInt("UserId", id).apply();
    }

}
