package com.sofac.fxmharmony.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.dto.UserDTO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.sofac.fxmharmony.Constants.PUSH_MESSAGES_STATE;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;


public class AppMethods {

    public static int getPxFromDp(int dimensionDp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

}
