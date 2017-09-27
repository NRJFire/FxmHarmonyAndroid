package com.sofac.fxmharmony.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.R.attr.id;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

/**
 * Created by Maxim on 07.09.2017.
 */

public class CheckAuthorization {

    private SharedPreferences preferences;

    public CheckAuthorization(Context context) {
        preferences = context.getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
    }

    public Boolean isAuthorization() {
        return preferences.getBoolean(IS_AUTHORIZATION, false);
    }

    public void setAuthorization(Boolean isAuthorization) {
        SharedPreferences.Editor editorUser = preferences.edit();
        editorUser.putBoolean(IS_AUTHORIZATION, isAuthorization);
        editorUser.apply();
        editorUser.commit();
    }
}
