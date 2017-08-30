package com.sofac.fxmharmony.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.dto.AppVersionDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import java.util.List;

/**
 * Created by Maxim on 21.08.2017.
 */

public class CheckVersionApp extends AsyncTask<String, Void, String> {
    ServerResponse<AppVersionDTO> serverResponse;
    Context context;
    String versionName = "";
    int versionCode = -1;
    AlertDialog.Builder builder;
    private FinishCheckVersion finishCheckVersion = null;

    public CheckVersionApp(Context context, FinishCheckVersion finishCheckVersion){
        this.context = context;
        builder = new AlertDialog.Builder(context);
        this.finishCheckVersion = finishCheckVersion;

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.versionName = packageInfo.versionName;
            this.versionCode = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public interface FinishCheckVersion {
        void processFinish(Boolean isFinishActivity);
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... urls) {
        ServerRequest<String> serverRequest = new ServerRequest<>(Constants.CHECK_VERSION, "");
        DataManager dataManager = DataManager.getInstance();
        serverResponse = dataManager.sendCheckVersion(serverRequest);
        if (serverResponse != null && Constants.REQUEST_SUCCESS.equals(serverResponse.getResponseStatus()))
            return Constants.REQUEST_SUCCESS;
        return Constants.SERVER_REQUEST_ERROR;
    }

    @Override
    protected void onPostExecute(String result) {
        if (Constants.REQUEST_SUCCESS.equals(result)) {
            if (versionCode!=serverResponse.getDataTransferObject().getVersionCode()) {
                if (serverResponse.getDataTransferObject().getImportant()) { // Important
                    builder.setCancelable(false);
                    builder.setOnCancelListener(null);
                    builder.setTitle("Check version");
                    builder.setMessage("This is very important version and you can't use this app. Please update your application from PlayMarket.\n \nNew version: " + serverResponse.getDataTransferObject().getVersionName() + "\nYour version: " + versionName);
                    builder.setPositiveButton("Update from PlayMarket", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openAppRating();
                            finishCheckVersion.processFinish(true);
                        }
                    });
                    builder.show();
                } else { // Not important
                    builder.setCancelable(true);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finishCheckVersion.processFinish(false);
                        }
                    });
                    builder.setTitle("Check version");
                    builder.setMessage("Please update your application from PlayMarket. \n \nNew version: " + serverResponse.getDataTransferObject().getVersionName() + "\nYour version: " + versionName);
                    builder.setPositiveButton("Update from PlayMarket", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openAppRating();
                        }
                    });
                    builder.setNegativeButton("Remember me later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishCheckVersion.processFinish(false);
                        }
                    });

                    builder.show();
                }
            } else {
                finishCheckVersion.processFinish(false);
            }

        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void openAppRating() {
        // you can also use BuildConfig.APPLICATION_ID
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+appId));
            context.startActivity(webIntent);
        }
    }
}
