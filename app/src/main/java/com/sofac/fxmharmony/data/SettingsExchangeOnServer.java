package com.sofac.fxmharmony.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.server.type.ServerRequest;
import com.sofac.fxmharmony.server.type.ServerResponse;


import timber.log.Timber;


public class SettingsExchangeOnServer<T> extends AsyncTask<String, Void, String> {

    private ServerResponse serverResponse;

    private String type;
    private T serverObject;
    private Context context;
    private ProgressDialog pd;

    public interface SettingsAsyncResponse {
        void processFinish(Boolean isSuccess);
    }

    private SettingsExchangeOnServer.SettingsAsyncResponse asyncResponse = null;

    public SettingsExchangeOnServer(T serverObject, String type, Context context, SettingsExchangeOnServer.SettingsAsyncResponse asyncResponse) {
        pd = new ProgressDialog(context, R.style.MyTheme);
        this.serverObject = serverObject;
        this.type = type;
        this.context = context;
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.show();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected String doInBackground(String... urls) {

        ServerRequest serverRequest = new ServerRequest(type, serverObject);
        DataManager dataManager = DataManager.getInstance();

        Timber.i(serverRequest.toString());

        serverResponse = dataManager.postSettingsRequest(serverRequest);

        if (serverResponse != null) {
            return serverResponse.getResponseStatus();
        }

        return Constants.SERVER_REQUEST_ERROR;
    }


    @Override
    protected void onPostExecute(String result) {
        Timber.e("Response Server: " + result);

        if (result.equals(Constants.REQUEST_SUCCESS)) {
            asyncResponse.processFinish(true);
        } else {
            Toast.makeText(context, R.string.errorConnection, Toast.LENGTH_SHORT).show();
            asyncResponse.processFinish(false);
        }
        pd.dismiss();
    }

}


