package com.sofac.fxmharmony.util;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.service.BackgroundFileUploadService;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

public class RequestMethods {


    public static void startServiceAttachLoadFilesToPost(Context context, ArrayList<Uri> uris, Long postID) {
        Intent serviceIntent = new Intent(context, BackgroundFileUploadService.class);
        serviceIntent.putExtra("type", Constants.ATTACH_LOAD_FXM_POST_FILES);
        serviceIntent.putExtra("uri", uris);
        serviceIntent.putExtra("serverObject", postID);
        context.startService(serviceIntent);
    }

    public static void startServiceAttachLoadAvatarToUser(Context context, Uri avatarUri) {
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(avatarUri);
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        Long userID = sharedPreferences.getLong(USER_ID_PREF, 0);

        Intent serviceIntent = new Intent(context, BackgroundFileUploadService.class);
        serviceIntent.putExtra("type", Constants.ATTACH_LOAD_USER_AVATAR);
        serviceIntent.putExtra("uri", uris);
        serviceIntent.putExtra("serverObject", userID);
        context.startService(serviceIntent);
    }


}
