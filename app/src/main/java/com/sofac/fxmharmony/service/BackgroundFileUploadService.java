package com.sofac.fxmharmony.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.server.type.ServerRequest;
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.util.PathUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.MULTIPART_FORM_DATA;

public class BackgroundFileUploadService extends Service {

    public BackgroundFileUploadService() {
    }

    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        executorService = Executors.newFixedThreadPool(16);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timber.i("On Start Command");

        Bundle extras = intent.getExtras();

        if (extras != null) {

            String type = extras.getString("type");
            Object serverObject = extras.get("serverObject");
            ArrayList<Uri> uris = (ArrayList<Uri>) extras.get("uri");

            executorService.execute(new FileExchangeRunnable<>(BackgroundFileUploadService.this, type, serverObject, uris));

        }

        return START_NOT_STICKY;
    }


    private class FileExchangeRunnable<T> implements Runnable {

        private ServerResponse<T> serverResponse;

        private String type;
        private T serverObject;
        private Context context;
        private DataManager dataManager;
        private Map<String, RequestBody> files;

        private Handler handler;

        public FileExchangeRunnable(Context context, String type, T serverObject, ArrayList<Uri> uris) {
            this.type = type;
            this.serverObject = serverObject;
            this.context = context;
            files = getFilesMapFromUri(context, uris);
            dataManager = DataManager.getInstance();
            handler = new Handler();

        }

        @Override
        public void run() {

            showToast(R.string.upload_start);

            ServerRequest serverRequest = new ServerRequest(type, null);

            Timber.i(serverRequest.toString());

            serverRequest.setDataTransferObject(serverObject);

            serverResponse = dataManager.postUploadFileRequest(serverRequest, files);

            if (serverResponse != null) {
                onEnd(serverResponse.getResponseStatus());
            }

            if (type.equals(Constants.ATTACH_LOAD_USER_AVATAR)) {
                String avatarName = (String) serverResponse.getDataTransferObject();
                AppMethods.saveAvatarImageName(context, avatarName);
            }

        }

        void onEnd(final String result) {

            Timber.i(result);

            if (result.equals(Constants.REQUEST_SUCCESS)) {
                showToast(R.string.upload_success);
            } else {
                showToast(R.string.errorConnection);
            }

        }


        private Map<String, RequestBody> getFilesMapFromUri(Context context, ArrayList<Uri> filesUris) {

            Map<String, RequestBody> files = new HashMap<>();

            for (Uri fileUri : filesUris) {

                try {
                    File file = new File(PathUtil.getPath(context, fileUri));

                    RequestBody requestBody = createRequestBody(file);

                    String key = String.format("%1$s\"; filename=\"%1$s", file.getName());
                    files.put(key, requestBody);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
            return files;
        }

        private RequestBody createRequestBody(@NonNull File file) {
            return RequestBody.create(
                    MediaType.parse(MULTIPART_FORM_DATA), file);
        }


        //  :(
        private void showToast(final int textResID) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, textResID, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}