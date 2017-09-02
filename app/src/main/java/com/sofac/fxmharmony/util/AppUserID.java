package com.sofac.fxmharmony.util;

import android.content.Context;
import android.content.SharedPreferences;


import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

/**
 * Created by Maxim on 02.09.2017.
 */

public class AppUserID {

    private SharedPreferences preferences;

    public AppUserID(Context context) {
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

}
