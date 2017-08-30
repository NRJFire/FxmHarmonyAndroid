package com.sofac.fxmharmony.util;

import android.os.AsyncTask;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Maxim on 26.07.2017.
 */

public class FileLoadingTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private File destination;
    private FileLoadingListener fileLoadingListener;
    private Throwable throwable;

    public FileLoadingTask(String url, File destination, FileLoadingListener fileLoadingListener) {
        this.url = url;
        this.destination = destination;
        this.fileLoadingListener = fileLoadingListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fileLoadingListener.onBegin();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            FileUtils.copyURLToFile(new URL(url), destination);
        } catch (IOException e) {
            throwable = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        fileLoadingListener.onEnd();
        if (throwable != null) {
            fileLoadingListener.onFailure(throwable);
        } else {
            fileLoadingListener.onSuccess();
        }
    }
}
