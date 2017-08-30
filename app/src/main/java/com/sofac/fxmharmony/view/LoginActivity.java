package com.sofac.fxmharmony.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sofac.fxmharmony.BuildConfig;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.dto.Authorization;
import com.sofac.fxmharmony.data.dto.ManagerInfoDTO;
import com.sofac.fxmharmony.data.dto.PermissionDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

/**
 * Activity login & password authorization, validation input field, if validate data start MainActivity.class
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static SharedPreferences preferences;
    Intent intent;
    private static long backPressed;
    EditText editPassword, editLogin;
    Button buttonLogin;



    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        buttonLogin.setOnClickListener(this);
        intent = new Intent(this, NavigationActivity.class);


//        try {
            ManagerInfoDTO.deleteAll(ManagerInfoDTO.class);
            PermissionDTO.deleteAll(PermissionDTO.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Timber.e("Clear DB");

    }

    private void initUI() {
        editPassword = (EditText) findViewById(R.id.editPassword);
        editLogin = (EditText) findViewById(R.id.editLogin);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    @Override
    public void onClick(View v) {
        String password = editPassword.getText().toString();
        String login = editLogin.getText().toString();
        if ("".equals(password) && "".equals(login)) {
            Toast.makeText(LoginActivity.this, getString(R.string.fieldEmpty), Toast.LENGTH_SHORT).show();
        } else {
            CheckAuthorizationOnServer task = new CheckAuthorizationOnServer();
            task.execute(editLogin.getText().toString(), editPassword.getText().toString());
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.ToastLogOut), Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    private class CheckAuthorizationOnServer extends AsyncTask<String, Void, String> {
        ServerResponse<ManagerInfoDTO> managerInfoServerResponse;
        ProgressDialog pd = new ProgressDialog(LoginActivity.this, R.style.MyTheme);

        @Override
        protected void onPreExecute() {
            pd.setCancelable(false);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            Authorization authorization = new Authorization(urls[0], urls[1], sharedPref.getString(Constants.GOOGLE_CLOUD_PREFERENCE, ""));

            ServerRequest serverRequest = new ServerRequest(Constants.AUTHORIZATION_REQUEST, authorization);
            Timber.e("!!!!!! serverRequest !!!!!"+serverRequest);
            DataManager dataManager = DataManager.getInstance();
            managerInfoServerResponse = dataManager.sendAuthorizationRequest(serverRequest);

            if (managerInfoServerResponse != null) {
                return managerInfoServerResponse.getResponseStatus();
            }
            return Constants.SERVER_REQUEST_ERROR;
        }

        @Override
        protected void onPostExecute(String result) {
            Timber.e("Response Server: " + result);

            if (Constants.REQUEST_SUCCESS.equals(result)) {

                ManagerInfoDTO managerInfoDTO = managerInfoServerResponse.getDataTransferObject();

                PermissionDTO permissionDTO = managerInfoServerResponse.getDataTransferObject().getPermissions();
                permissionDTO.setId(managerInfoDTO.getIdServer());

                Timber.e(managerInfoDTO.toString());
                Timber.e(permissionDTO.toString());

                managerInfoDTO.save();
                permissionDTO.save();


                preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(IS_AUTHORIZATION, true);
                editor.apply();
                editor.commit();

                SharedPreferences preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
                SharedPreferences.Editor editorUser = preferences.edit();
                editorUser.putLong(USER_ID_PREF, managerInfoDTO.getIdServer());
                editorUser.apply();
                editorUser.commit();

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, R.string.errorConnection, Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = editLogin.getText().toString();
        PasswordValue = editPassword.getText().toString();
        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        editLogin.setText(UnameValue);
        editPassword.setText(PasswordValue);
        System.out.println("onResume load name: " + UnameValue);
        System.out.println("onResume load password: " + PasswordValue);
    }




}
