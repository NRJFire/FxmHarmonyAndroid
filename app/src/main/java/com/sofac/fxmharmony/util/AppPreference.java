package com.sofac.fxmharmony.util;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.dto.UserDTO;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.GOOGLE_CLOUD_PREFERENCE;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

/**
 * Created by Maxim on 02.09.2017.
 */

public class AppPreference {

    private SharedPreferences preferences;
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();

    public AppPreference(Context context) {
        preferences = context.getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
    }

    public Long getID() {
        return preferences.getLong(USER_ID_PREF, 1L);
    }

    public void setID(Long id) {
        SharedPreferences.Editor editorUser = preferences.edit();
        editorUser.putLong(USER_ID_PREF, id);
        editorUser.apply();
        editorUser.commit();
    }

    public Boolean getAuthorization() {
        return preferences.getBoolean(IS_AUTHORIZATION, false);
    }

    public void setAuthorization(Boolean isAuthorization) {
        SharedPreferences.Editor editorUser = preferences.edit();
        editorUser.putBoolean(IS_AUTHORIZATION, isAuthorization);
        editorUser.apply();
        editorUser.commit();
    }

    public String getGoogleKey() {
        return preferences.getString(GOOGLE_CLOUD_PREFERENCE, "");
    }

    public void setGoogleKey(String googleKey) {
        SharedPreferences.Editor editorUser = preferences.edit();
        editorUser.putString(GOOGLE_CLOUD_PREFERENCE, googleKey);
        editorUser.apply();
        editorUser.commit();
    }

    public UserDTO getUser() {
        return gson.fromJson(preferences.getString(APP_PREFERENCES, ""), new TypeToken<UserDTO>(){}.getType());
    }

    public void setUser(UserDTO userDTO) {
        SharedPreferences.Editor editorUser = preferences.edit();
        editorUser.putString(APP_PREFERENCES, gson.toJson(userDTO));
        editorUser.apply();
        editorUser.commit();
        setID(userDTO.getId());
    }

}
