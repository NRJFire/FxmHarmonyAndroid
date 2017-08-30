package com.sofac.fxmharmony.util.googleFirebaseService;



import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sofac.fxmharmony.Constants;

import timber.log.Timber;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.i("Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        saveCloudMessageIdToPreferences(refreshedToken);

    }

    private void saveCloudMessageIdToPreferences(String token) {
        SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.GOOGLE_CLOUD_PREFERENCE , token);
        editor.apply();
    }

}
